package com.example.test.sbre.jwt;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.build.ToStringPlugin.Enhance.Prefix;

// 토큰 생성 및 관련된 작업을 여기다가 다 넣을거임 
@Service
public class JwtService {
	
	// 토큰의 만료 시간 저장 변수 ( 단위 : ms )         //1분    //1초
	static final long EXPIRATIONTIME = 24 * 60 * 60* 1000; //만료 시간이고 고정, 상수로 설정 
	
	// 토큰 타입 ( 응답 헤더에 사영될 접두어 )
	static final String PREFTX = "Bearer ";
	
	// 임시 서명 키  : 서명도 일치해야 통과하는 거기 때문에 // 임시로 만든거임 
								// 랜덤한 일련번호가 생김 //알고리즘 암호화 
	static final SecretKey KEY = Jwts.SIG.HS256.key().build();
	
	//권한 관련 클레임 키
	static final String ROLES_CLAIM = "roles";

	//토큰 생성하는 메서드! // 다양한 정보를 심어줄 수 있다보니 매개변수를 토큰 생성에 필요한 데이터를 매개변수로 받아올거임 (username, 권한! (roleList) // 자료형과 변수! 
	public String createToken (String username, Collection<? extends GrantedAuthority> authorities ) {
	
		// 발급시간이 필요하고 // 만료 시간까지 셋팅을 해줘야함 
		Date now = new Date();
		//자바.유틸
		Date exp = new Date(now.getTime() + EXPIRATIONTIME);
							//현재 시점 기준 + 만료 시간 // 만료일자 ! 
		// 권한 담아줄 리스트 생성 : 권한이 한 사람에게 여러 개가 설정되어 있을 수도 있어서 컬렉션으로 리턴하고 여러개 설정한거임 
		List<String> roles = (authorities == null) ? 
								List.of()  // 참이면 권한이 없으니까 리스트로 리턴 
								: authorities.stream()          // 스트림 열어서 컬렉션에 있는거 하나하나 꺼내서 리스트로 만든다음에 roles에 담아줘1 
									.map(GrantedAuthority::getAuthority)
									.toList();

		return Jwts.builder()							// p.384
				.subject(username)  //sub          		//토큰 값에 넣을거 하나하나 셋팅해주는거임 
				.issuedAt(now)		//iat			    // 토큰 기본 설정된 클레임 
				.expiration(exp)	//exp	
				.signWith(KEY)		//서명
				.claim(ROLES_CLAIM, roles) //커스텀 클래임 // 새 클레임 설정하고 싶을 때, 키 밸류 형태로 설정 
				.compact();	
	}
	
	
	/* 
	 요청 객체에서 헤더에 Authoriztion에 있는 토큰만 추출하는 메서드 
	*/												// 요청 객체임 
	public String resolveToken (HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
	
		if(header != null && header.startsWith(PREFTX))  {   //Bearer 이거니까 토큰이라면 추출해라! 
			return header.substring(PREFTX.length()).trim();     // 내가 원하는 지점부터 문자열 추출해 //프리픽스 뒤부터 끝까지 // 공백 제거 
		}
		return null;
	}

	/**
	 * 토큰 유효성 검사하는 메서드 
	 * @param 매개변수 넣으셈 
	 */        
	
	//    맞다, 아니다            토큰을 받아와서 유효성 검사할거니까 */
	public boolean validate(String token) {
		if(token == null || token.isBlank())
			return false;
	// 예외 발생이 있을 수 있어서 try.catch로 설정해 줄거임
		try {
			Jwts.parser()
				.verifyWith(KEY)  // 서명 지정 
				.clockSkewSeconds(30) // 30초 정도 오차는 허용 (선택 사항)
				.build()
				.parseSignedClaims(token);
				return true;
		} catch (ExpiredJwtException e) {
			return false;		// 만료
		} catch (JwtException e) {
			return false; // 서명, 형식, 등등 검증 실패
		} catch (Exception e) {
			return false; // 기타 예외 (안전 빵으로)
		}				// 그때 그때 예외발생하면 리턴되니까 final 설정 안함! 
	
	} 
	
	// 서버에서는 세션엔 따로 저장을 하지 않고 주고 딱 끝이라서 username이나 권한을 뽑아내야 할 때가 있어서 이건 직접 설정해 줘야함 
	
	/**
	 * 토큰에서 subject(username) 꺼내주는 메서드
	*/
	public String getUsername(String token) {
		Claims claims = Jwts.parser()
						  .verifyWith(KEY)
						  .build()
						  .parseSignedClaims(token)	
						  .getPayload();
		return claims.getSubject();  // 토큰을 설정했던 것처럼 이 방식으로 토큰의 부분 값을 추출할 수 있음 
	
	}			

  /**
   * 토큰에서 권한(role) 꺼내주는 메서드 
   */
	
	public List<? extends GrantedAuthority> getAuthorities(String token) {
			Claims claims = Jwts.parser()
				  .verifyWith(KEY)
				  .build()
				  .parseSignedClaims(token)	
				  .getPayload();
	
			Object raw = claims.get(ROLES_CLAIM); //이미 상수로 만들어 놓은거 뺀거임 
 		 
			if(raw instanceof List<?> list) { // 값이 맞으면 바로 집어 넣어버림 (형변환되어서 List라는 변수에 담김) 
 				return list.stream()   // 하나하나 처리하려고 stream을 열고 하나하나 다 추가해줌 
						.filter(String.class::isInstance)  // true인 것마 남겨주고 // 자료형이 문자열이냐 아니냐를 물어봄 (filter는 참인 것만 남기고 빼버림, 문자열이 아닌 게 있다면 다 날려버려라 )  
						.map(String.class::cast) 			// 반복적으로 처리해 줌 // cast 형변환해줌 // 오브젝트 형태로 되어 있으면 stream으로 형변환 시켜라 
						.map(SimpleGrantedAuthority::new)   // 형변환된 문자열을 가지고 권한 객체를 생성하는거임 (권한 객체로 해야지 문자열로는 안되기 때문) // 객체 생성해준거임 
						.collect(Collectors.toList());     // 처리된 걸 모아서 리스트 형태로 만들어주는거임 
			}
			return List.of();
			
			}
	
}

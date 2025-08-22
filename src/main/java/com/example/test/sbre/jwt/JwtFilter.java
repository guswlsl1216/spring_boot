package com.example.test.sbre.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 글쓰기 작업할 때도 토큰 검사가 필요함! 


// 요청에 들어왔을 때 컨트롤러 가기전에 처리되는 메서드!
// 얘는 필터임! 로그인 사용자 <=> 서버화면 중간 필터! 

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;  //서비스에 다 만들어뒀으니까 의존성 주입 ! 
	
	
	
	@Override									// 요청 객체 //여기서 토큰 빼오면 됨 									
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//헤어데엇 토큰이 있나 없나 검사하고 => 토큰이 있으면 토큰 검증을 해야 함 (유효한지 안한지) => 넘어갈 수 있게 끔
		
//		String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);  //헤더로 있으면 꺼내도 되지만, 서비스에서 bears로 빼는거 메서드 생성해둬서 그거 쓸거임

		String jwt = jwtService.resolveToken(request); 
		boolean check = jwtService.validate(jwt); // 유효한지 검사하고
		
		
		if( check ) {  // 토큰이 유효할 때 // 인증 객체에 저장되게 해줄거임 // 서비스에서 유저네임 빼오는 메서드 쓸거임
			// 토큰에 있는 사용자 이름과 권한을 꺼내온거임 
			String username = jwtService.getUsername(jwt);     
			List<? extends GrantedAuthority> roles = jwtService.getAuthorities(jwt);
			
			// 인증 객체 만든거임 
			Authentication auth = 							// null은 pw인데 이미 검증 다한거고 여기서는 pw 필요없어서 null로 설정 
					new UsernamePasswordAuthenticationToken(username, null, roles);  
		
			SecurityContextHolder.getContext().setAuthentication(auth); // 시큐리티에 등록하는 과정 // 그래야 글작성이랑 다른 기능이 가능함  
			
		}
	
		filterChain.doFilter(request, response);  //요청, 응답 객체 필터로 다 넘김 
	
	}

}

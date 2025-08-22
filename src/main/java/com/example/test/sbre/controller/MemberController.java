package com.example.test.sbre.controller;



import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.sbre.domain.Member;
import com.example.test.sbre.jwt.JwtService;
import com.example.test.sbre.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/signup")					// 객체 받아서
	public ResponseEntity<?> signup(@RequestBody Member member) {
		
		memberService.insert(member);
		
		return new ResponseEntity<>("회원가입 성공!", HttpStatus.OK);
	} 
	
	@PostMapping("/login")						// 아이디, 비번 받아서 
	public ResponseEntity<?> login(@RequestBody Member member) {
		UsernamePasswordAuthenticationToken cred = 
				new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());
	
		Authentication auth = authenticationManager.authenticate(cred); //인증 좀 해보라고 시키는거임 => 
																		//인증 결과인 인증 객체로 변수에 담아야함 
		String jwt = jwtService.createToken(auth.getName(), auth.getAuthorities());	// 인증 성공한 사람, 토큰을 발급해 줄거임 , 변수 하나 만들어서 
													//유저네임(principal까지 포함된 getName  , 권한 목록 )
		
		return ResponseEntity.ok()
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)   // 헤더에 토큰을 심어줘야 함 ! 
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization") // 크로스오리진 때문에 프론트에서 못읽을 수더 있어서 설정해 주는거임 
					.build();										
	}

	@GetMapping("/userinfo")
	public ResponseEntity<?> userInfo (Authentication auth) {
	
		Member member = memberService.getMember(auth.getName());
		
	//		System.out.println(auth.getName());
		return new ResponseEntity<>(member, HttpStatus.OK);
	}
	
	// 스프링 : 토큰 // 리액트는 세션 //
	// 두 프로그램이 세션을 공유하지 않지만 시큐리티에 로그인한 사용자가 등록이 되긴 함 
	// jwt 서비스에서 토큰 검사, 검증하고 검증된 애를 객체로 만들어서 인증 객체를 시큐리티에 등록시켜 버림 
	
}

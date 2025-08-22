package com.example.test.sbre.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 인증 실패했을 때 구현하는 구현체 
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

	// 인증 실패했을 때 실행되는 메서드
	//추상 메서드를 오버라이딩 해줄거임
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// 응답 객체에 담아줄거라서 response로! 
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write("인증 실패"); // write로 직접 써주는 거임!
		
		// 만들고 시큐리티한테 등록만 해주면 됨! 
		
		
	}

	
	
	
}

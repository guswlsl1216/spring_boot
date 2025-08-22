package com.example.test.sbre.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.test.sbre.jwt.JwtFilter;
import com.example.test.sbre.security.AuthEntryPoint;

// 시큐리티 관련된 설정을 해주는 클래스 

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private AuthEntryPoint authEntryPoint;
	

	@Bean  // 체크할 필터를 만든거임 
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	http.csrf(csrf -> csrf.disable())  								// 토큰 안쓸거야! 
		.cors(cors -> cors.configurationSource(getSource()))     //크로스 오리진 설정 => 이런 쪽에서 요청이 들어오면 허용해줄거야 
		.exceptionHandling(eh -> 
			eh.authenticationEntryPoint(authEntryPoint)        // 구현체에서 오버라이딩한 메서드를 여기서 호출해줌 ! 
		)
		
		.authorizeHttpRequests(auth -> 
			auth.requestMatchers("/login", "/signup").permitAll()
				.anyRequest().authenticated()
		);
		//jwt filter 적용되게 해달라고 설정해 줘야함 (jwt 필터 만든 후 여기로 넘어와서 아래 코드 추가한거임 )
	
		// addFilterBefore : 필터 추가해라!
	
							//2. 이거 먼저 실행되게 해줘 // 1. 이거 작동되기 전에
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	CorsConfigurationSource getSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
	
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
	
		return source;
	}

	@Bean //암호화해서 줄려고 
	PasswordEncoder passwordEncoder ( ) {
		return new BCryptPasswordEncoder();
	}
	
	// 인증 매니저 불러다 쓰기 위해서 bean으로 등록해서 주입받아서 쓸거임
	
	@Bean // 미리 설정 
	AuthenticationManager manager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
		
	}
}

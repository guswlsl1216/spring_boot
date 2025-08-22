package com.example.test.sbre.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration						// 메서드들이 내장되어 있음 (cors 오류 때문에)
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") //endpoint url 넣어주는거임 // 다 허용하겠다! 
				.allowedOrigins("http://localhost:5173");  // 허용할 주소 : 우리 리액트 주소 	
	
// 이거 빼면 스프링 작업은 다 비슷함! 		
	}

}

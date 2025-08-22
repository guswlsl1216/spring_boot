package com.example.test.sbre.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.test.sbre.config.WebConfig;
import com.example.test.sbre.domain.TestVO;

@RestController
public class TestController {

    private final WebConfig webConfig;

    TestController(WebConfig webConfig) {
        this.webConfig = webConfig;
    }

	@GetMapping("/test")
	public String test() {
		return "Hello React & Spring";
		
	}
	
	@GetMapping("/test/info")
	public TestVO info() {
		// DB에서 가져온 정보라고 가정! 
		TestVO vo = new TestVO("qwer", "1234", 20); //객체 하나 만들어서 id, pw, id 입력  
		
		return vo;  //vo리턴시킬거니까 리턴타입 TestVO //리액트에서 요청해서 받아지나 확인해볼거임 
	}
	
	@PostMapping("/test/{no}")  
	public void test2(@PathVariable int no, @RequestBody TestVO vo, String msg)  {
		System.out.println(no);
		System.out.println(vo.toString());
		System.out.println(msg);
		
	}
}

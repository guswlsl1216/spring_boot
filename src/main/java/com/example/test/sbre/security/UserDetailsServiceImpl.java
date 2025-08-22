package com.example.test.sbre.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.test.sbre.Repository.MemberRepository;
import com.example.test.sbre.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService  {

	private final MemberRepository memberRepository;
	
	
	@Override								// DB에서 username 꺼내와서      USErDeatail 객체를 만들어줘야 함 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Member member = memberRepository.findByUsername(username).get(); // 사용자가 로그인하려는 유저의 레코드가 들어있음 
		
		
		return new UserDetailsImpl(member); // 객체를 생성해서 리턴해 줘야 하는데 userDetail에 member를 넣어서 보냄 
	}
 
	
	
	
}

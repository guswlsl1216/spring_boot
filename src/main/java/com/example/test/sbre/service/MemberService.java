package com.example.test.sbre.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.test.sbre.Repository.MemberRepository;
import com.example.test.sbre.domain.Member;
import com.example.test.sbre.domain.RoleType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  // 생성자 통한 의존성 주입
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public void insert(Member member) {
		member.setPassword(passwordEncoder.encode(member.getPassword()));  //암호화 된거 넣어주기 
		member.setRole(RoleType.USER);
		
		memberRepository.save(member);
	}

	// 유저 네임 가져오기 
	public Member getMember(String username) {
		
		return memberRepository.findByUsername(username).get();
	}

}

package com.example.test.sbre.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.test.sbre.domain.Member;

import lombok.Getter;
import lombok.Setter;

// 유저 디테일 구현하는 구현체! 

// 얘를 왜 만들어야 하냐면...?
// security에 있는건데 userDetail을 써도 되긴 하지만 
// 

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Member member; // 엔티티로 만든 객체를 넣어줌 //아니면 그냥 멤버변수 한땀 한땀 써줘도 됨 

	public UserDetailsImpl(Member member) {  // 생성자 호출
		
		this.member = member;
	
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> roleList = new ArrayList<>();
		
		roleList.add(()-> {
			return "ROLE_" + member.getRole();
		});
	
		return roleList;
	// 인증 객체가 만들어지면 권한들이 들어있어서 그 권한을 리턴시켜주는 메서드를 오버라이딩 한거임 
		
	}

	@Override
	public String getPassword() {
		
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return member.getUsername();
	}
				
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}

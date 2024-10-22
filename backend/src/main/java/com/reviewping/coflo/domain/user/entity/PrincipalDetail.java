package com.reviewping.coflo.domain.user.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class PrincipalDetail implements UserDetails {

	private User user;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean isNewMember;

	private Map<String, Object> attributes;

	public PrincipalDetail(User user, Collection<? extends GrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}

	public PrincipalDetail(User user, Collection<? extends GrantedAuthority> authorities,
		Map<String, Object> attributes) {
		this.user = user;
		this.authorities = authorities;
		this.attributes = attributes;
	}

	public PrincipalDetail(User user, boolean isNewMember, Collection<? extends GrantedAuthority> authorities,
		Map<String, Object> attributes) {
		this.user = user;
		this.isNewMember = isNewMember;
		this.authorities = authorities;
		this.attributes = attributes;
	}

	// info 에 들어가는 것들이 토큰에 들어가는 데이터
	public Map<String, Object> getMemberInfo() {
		Map<String, Object> info = new HashMap<>();
		info.put("id", user.getId());
		info.put("name", user.getUsername());
		info.put("role", user.getRole());
		return info;
	}

	public Long getId() {
		return user.getId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return user.getId() + "";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
package com.reviewping.coflo.domain.user.entity;

import com.reviewping.coflo.domain.user.enums.Provider;
import com.reviewping.coflo.domain.user.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String userToken;

	private String profileImageUrl;

	private Long oauth2Id;

	private Provider provider;

	private Role role;

	@Builder
	public User(String username, String userToken, String profileImageUrl, Long oauth2Id, Provider provider,
		Role role) {
		this.username = username;
		this.userToken = userToken;
		this.profileImageUrl = profileImageUrl;
		this.oauth2Id = oauth2Id;
		this.provider = provider;
		this.role = role;
	}
}

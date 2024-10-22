package com.reviewping.coflo.domain.user.entity;

import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import com.reviewping.coflo.global.crypto.CryptoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitlabAccount extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private String domain;

	@Convert(converter = CryptoConverter.class)
	@Column(nullable = false, unique = true)
	private String userToken;

	@Builder
	public GitlabAccount(User user, String domain, String userToken) {
		this.user = user;
		this.domain = domain;
		this.userToken = userToken;
	}
}

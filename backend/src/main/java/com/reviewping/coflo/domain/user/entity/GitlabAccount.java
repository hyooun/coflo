package com.reviewping.coflo.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.reviewping.coflo.domain.userproject.entity.UserProject;
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
import jakarta.persistence.OneToMany;
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

	@OneToMany(mappedBy = "gitlabAccount")
	private List<UserProject> userProjects = new ArrayList<>();

	@Builder
	public GitlabAccount(User user, String domain, String userToken) {
		this.user = user;
		this.domain = domain;
		this.userToken = userToken;
		user.getGitlabAccounts().add(this);
	}
}

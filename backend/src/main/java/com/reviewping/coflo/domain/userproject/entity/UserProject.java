package com.reviewping.coflo.domain.userproject.entity;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProject extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gitlab_account_id")
	private GitlabAccount gitlabAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	public UserProject(GitlabAccount gitlabAccount, Project project) {
		this.gitlabAccount = gitlabAccount;
		this.project = project;
		gitlabAccount.getUserProjects().add(this);
		project.getUserProjects().add(this);
	}

}

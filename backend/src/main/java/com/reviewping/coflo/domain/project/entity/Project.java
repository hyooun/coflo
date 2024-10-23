package com.reviewping.coflo.domain.project.entity;

import java.util.ArrayList;
import java.util.List;

import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String botToken;

	@Column(nullable = false, unique = true)
	private Long gitlabProjectId;

	private String name;

	@OneToMany(mappedBy = "project")
	private List<UserProject> userProjects = new ArrayList<>();

	@Builder
	public Project(String botToken, Long gitlabProjectId, String name) {
		this.botToken = botToken;
		this.gitlabProjectId = gitlabProjectId;
		this.name = name;
	}
}

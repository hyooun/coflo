package com.reviewping.coflo.domain.userproject.entity;

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
public class UserProjectScore extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_project_id")
	private UserProject userProject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "software_quality_id")
	private SoftwareQuality softwareQuality;

	public UserProjectScore(UserProject userProject, SoftwareQuality softwareQuality) {
		this.userProject = userProject;
		this.softwareQuality = softwareQuality;
		userProject.getUserProjectScores().add(this);
	}

}

package com.reviewping.coflo.domain.gitlab.service;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectResponse;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.util.RestTemplateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitLabService {

	@Value("${gitlab.api.project-list}")
	private String projectListEndpoint;

	public List<GitlabProjectResponse> getProjects(String gitlabUrl, String token) {
		HttpHeaders headers = RestTemplateUtils.createHeaders("application/json", token);
		String url = gitlabUrl + projectListEndpoint;

		try {
			ResponseEntity<List<GitlabProjectResponse>> response = RestTemplateUtils.sendGetRequest(
				url,
				headers,
				new ParameterizedTypeReference<List<GitlabProjectResponse>>() {
				});
			handleResponseStatus(response.getStatusCode());
			return response.getBody();
		} catch (BusinessException ex) {
			throw new BusinessException(EXTERNAL_API_INTERNAL_SERVER_ERROR);
		}
	}

	private void handleResponseStatus(HttpStatusCode response) {
		if (response.is4xxClientError()) {
			log.error("GitLab client error: {}", response);
			throw new BusinessException(ErrorCode.EXTERNAL_API_BAD_REQUEST);
		} else if (response.is5xxServerError()) {
			log.error("GitLab server error: {}", response);
			throw new BusinessException(ErrorCode.EXTERNAL_API_INTERNAL_SERVER_ERROR);
		} else if (response.isError()) {
			log.error("Unexpected error: {}", response);
			throw new BusinessException(ErrorCode.EXTERNAL_API_INTERNAL_SERVER_ERROR);
		}
	}
}


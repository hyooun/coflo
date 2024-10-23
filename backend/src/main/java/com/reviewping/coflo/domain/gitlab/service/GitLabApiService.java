package com.reviewping.coflo.domain.gitlab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabProjectResponse;
import com.reviewping.coflo.domain.gitlab.dto.response.GitlabUserInfoResponse;
import com.reviewping.coflo.global.util.RestTemplateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitLabApiService {

	@Value("${gitlab.api.project-search}")
	private String projectListEndpoint;

	@Value("${gitlab.api.user-info}")
	private String userInfoEndpoint;

	private static final String URL_PROTOCOL_HTTPS = "https://";
	private static final String MIME_TYPE_JSON = "application/json";

	public List<GitlabProjectResponse> searchGitlabProjects(String gitlabUrl, String token, String searchQuery) {
		HttpHeaders headers = RestTemplateUtils.createHeaders(MIME_TYPE_JSON, token);
		String url = URL_PROTOCOL_HTTPS + gitlabUrl + projectListEndpoint + searchQuery;

		ResponseEntity<List<GitlabProjectResponse>> response =
			RestTemplateUtils.sendGetRequest(
				url,
				headers,
				new ParameterizedTypeReference<List<GitlabProjectResponse>>() {
				});
		return response.getBody();
	}

	public GitlabUserInfoResponse getUserInfo(String gitlabUrl, String token) {
		HttpHeaders headers = RestTemplateUtils.createHeaders(MIME_TYPE_JSON, token);
		String url = URL_PROTOCOL_HTTPS + gitlabUrl + userInfoEndpoint;

		ResponseEntity<GitlabUserInfoResponse> response =
			RestTemplateUtils.sendGetRequest(
				url, headers, new ParameterizedTypeReference<GitlabUserInfoResponse>() {
				});
		return response.getBody();
	}
}

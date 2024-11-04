package com.reviewping.coflo.service.dto;

public record InitRequestMessage(Long projectId, String gitUrl, String branch, String token) {}

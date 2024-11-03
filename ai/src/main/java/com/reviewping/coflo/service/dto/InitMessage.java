package com.reviewping.coflo.service.dto;

public record InitMessage(Long projectId, String gitUrl, String branch, String token) {}

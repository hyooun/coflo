package com.reviewping.coflo.global.integration;

public record InitMessage(Long projectId, String gitProjectUrl, String branch, String token) {}

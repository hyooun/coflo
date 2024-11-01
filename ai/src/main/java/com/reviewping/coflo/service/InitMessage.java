package com.reviewping.coflo.service;

public record InitMessage(Long projectId, String gitUrl, String branch, String token) {}

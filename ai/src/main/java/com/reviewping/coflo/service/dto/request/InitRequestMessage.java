package com.reviewping.coflo.service.dto.request;

public record InitRequestMessage(
        Long projectId, Long branchId, String gitUrl, String branch, String token) {}

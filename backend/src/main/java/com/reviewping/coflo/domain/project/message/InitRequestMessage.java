package com.reviewping.coflo.domain.project.message;

public record InitRequestMessage(
        Long projectId, Long branchId, String gitUrl, String branch, String token) {}

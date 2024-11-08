package com.reviewping.coflo.domain.project.message;

public record UpdateRequestMessage(
        Long projectId,
        Long branchId,
        String gitUrl,
        String branch,
        String token,
        String commitHash) {}

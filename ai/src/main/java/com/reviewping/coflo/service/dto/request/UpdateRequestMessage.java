package com.reviewping.coflo.service.dto.request;

public record UpdateRequestMessage(
        Long projectId,
        Long branchId,
        String gitUrl,
        String branch,
        String token,
        String commitHash) {}

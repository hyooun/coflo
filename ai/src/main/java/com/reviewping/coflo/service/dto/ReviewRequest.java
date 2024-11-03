package com.reviewping.coflo.service.dto;

public record ReviewRequest(
        Long projectId,
        Long mrInfoId,
        String branch,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl) {}

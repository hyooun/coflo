package com.reviewping.coflo.service.dto;

public record ReviewRequestMessage(
        Long projectId,
        Long mrInfoId,
        String branch,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl) {}

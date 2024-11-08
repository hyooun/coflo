package com.reviewping.coflo.domain.mergerequest.controller.dto.request;

public record GitlabMrPageRequest(String state, int size, int page, String keyword) {}

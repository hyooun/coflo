package com.reviewping.coflo.domain.userproject.controller.dto.request;

import java.util.List;

public record ProjectLinkRequest(String botToken, List<String> branches) {}

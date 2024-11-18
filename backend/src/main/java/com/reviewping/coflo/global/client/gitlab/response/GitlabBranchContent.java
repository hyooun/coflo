package com.reviewping.coflo.global.client.gitlab.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitlabBranchContent(
        String name,
        @JsonProperty("default") Boolean isDefault,
        @JsonProperty("protected") Boolean isProtected) {}

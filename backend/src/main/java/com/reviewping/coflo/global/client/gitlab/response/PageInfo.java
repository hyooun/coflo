package com.reviewping.coflo.global.client.gitlab.response;

public record PageInfo(
        String startCursor, boolean hasNextPage, boolean hasPreviousPage, String endCursor) {}

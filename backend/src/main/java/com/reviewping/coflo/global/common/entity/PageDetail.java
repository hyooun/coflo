package com.reviewping.coflo.global.common.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PageDetail {
    private long totalElements;
    private int totalPages;
    private boolean isLast;
    private int currPage;
}

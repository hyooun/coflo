package com.reviewping.coflo.domain.project.vo;

import java.time.LocalDate;

public record ProjectWeek(int startWeek, int endWeek, LocalDate startDate, LocalDate endDate) {}

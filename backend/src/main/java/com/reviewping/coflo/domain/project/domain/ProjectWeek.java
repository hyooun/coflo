package com.reviewping.coflo.domain.project.domain;

import java.time.LocalDate;

public record ProjectWeek(int startWeek, int endWeek, LocalDate startDate, LocalDate endDate) {}

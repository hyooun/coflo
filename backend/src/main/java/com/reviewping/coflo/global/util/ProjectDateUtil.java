package com.reviewping.coflo.global.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import org.springframework.stereotype.Component;

@Component
public class ProjectDateUtil {
    /**
     * 주어진 프로젝트 생성일과 현재 날짜를 기반으로 몇 주차인지 계산
     */
    public static int calculateWeekNumber(LocalDate projectCreatedDate, LocalDate currentDate) {
        LocalDate projectStartMonday = projectCreatedDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate currentMonday = currentDate.with(java.time.DayOfWeek.MONDAY);
        long weeksBetween = ChronoUnit.WEEKS.between(projectStartMonday, currentMonday);
        return (int) weeksBetween + 1;
    }

    /**
     * 주어진 프로젝트 시작 날짜와 주차에 대해 해당 주차의 시작일과 종료일을 계산
     */
    public static LocalDate[] calculateWeekStartAndEndDates(
            LocalDate projectCreatedDate, int weekNumber) {
        LocalDate projectStartMonday = projectCreatedDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate startOfWeek = projectStartMonday.plusWeeks(weekNumber - 1);
        LocalDate endOfWeek =
                startOfWeek.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        return new LocalDate[] {startOfWeek, endOfWeek};
    }
}

package com.reviewping.coflo.global.util;

import com.reviewping.coflo.domain.project.domain.ProjectWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import org.springframework.stereotype.Component;

@Component
public class ProjectDateUtil {
    /**
     * 주어진 프로젝트 생성일과 현재 날짜를 기반으로 몇 주차인지 계산
     */
    public int calculateWeekNumber(LocalDate projectCreatedDate, LocalDate currentDate) {
        LocalDate projectStartMonday = projectCreatedDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate currentMonday = currentDate.with(java.time.DayOfWeek.MONDAY);
        long weeksBetween = ChronoUnit.WEEKS.between(projectStartMonday, currentMonday);
        return (int) weeksBetween + 1;
    }

    /**
     * @param projectCreatedDate 프로젝트 연동일
     * @param weekNumber         n주차
     * @return 시작일, 종료일
     */
    public LocalDate[] calculateWeekStartAndEndDates(LocalDate projectCreatedDate, int weekNumber) {
        LocalDate projectStartMonday = projectCreatedDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate startOfWeek = projectStartMonday.plusWeeks(weekNumber - 1);
        LocalDate endOfWeek =
                startOfWeek.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        return new LocalDate[] {startOfWeek, endOfWeek};
    }

    /**
     * @param projectCreatedDate 프로젝트 연동일
     * @param currentDate        오늘 날짜
     * @return 시작일, 종료일
     */
    public LocalDate[] calculateWeekStartAndEndDates(
            LocalDate projectCreatedDate, LocalDate currentDate) {
        int weekNumber = this.calculateWeekNumber(projectCreatedDate, currentDate);
        return calculateWeekStartAndEndDates(projectCreatedDate, weekNumber);
    }

    public ProjectWeek calculateWeekRange(
            LocalDate projectCreatedDate, int period, LocalDate currentDate) {
        int currentWeek = calculateWeekNumber(projectCreatedDate, currentDate);
        int startWeek = Math.max(1, currentWeek - period);
        int endWeek = currentWeek - 1;

        LocalDate projectStartMonday = projectCreatedDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate startDate = projectStartMonday.plusWeeks(startWeek - 1);
        LocalDate endDate =
                projectStartMonday
                        .plusWeeks(endWeek - 1)
                        .with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        return new ProjectWeek(startWeek, endWeek, startDate, endDate);
    }
}

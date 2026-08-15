package com.sky.studentmanagement.service.impl;

import com.sky.studentmanagement.dto.DashboardListDto;
import com.sky.studentmanagement.dto.StatsDto;
import com.sky.studentmanagement.repository.CourseRepo;
import com.sky.studentmanagement.repository.EnrollmentRepo;
import com.sky.studentmanagement.repository.StudentRepo;
import com.sky.studentmanagement.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;
    private final EnrollmentRepo enrollmentRepo;

    public DashboardServiceImpl(StudentRepo studentRepo, CourseRepo courseRepo, EnrollmentRepo enrollmentRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    @Override
    public StatsDto getStats() {
        logger.info("Get stats from dashboard service called.");
        try {
            long totalStudents = studentRepo.countByActiveTrue();
            long totalCourses = courseRepo.countByActiveTrue();

            // Safe fetch and extraction for topCourse
            List<Object[]> listResult = enrollmentRepo.findTopCourse(PageRequest.of(0, 1));
            Map.Entry<String, Long> topCourse = Map.entry("N/A", 0L);

            if (listResult != null && !listResult.isEmpty()) {
                Object[] result = listResult.get(0);
                if (result != null && result.length >= 2 && result[0] != null && result[1] != null) {
                    topCourse = Map.entry(
                            String.valueOf(result[0]),
                            Long.valueOf(String.valueOf(result[1]))
                    );
                }
            }

            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            long todayCount = enrollmentRepo.countTodayEnrollments(startOfDay, endOfDay);

            YearMonth currentMonth = YearMonth.now();
            LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);

            long monthCount = enrollmentRepo.countMonthEnrollments(startOfMonth, endOfMonth);

            Map.Entry<Long, Long> newRegistrations = Map.entry(monthCount, todayCount);

            return StatsDto.builder()
                    .totalStudents(totalStudents)
                    .totalCourses(totalCourses)
                    .topCourse(topCourse)
                    .newRegistrations(newRegistrations)
                    .build();

        } catch (Exception e) {
            logger.error("Error occurred in getStats service call", e);
        }

        // Return a fully initialized object to prevent Thymeleaf SpEL exceptions when errors occur
        return StatsDto.builder()
                .totalStudents(0L)
                .totalCourses(0L)
                .topCourse(Map.entry("N/A", 0L))
                .newRegistrations(Map.entry(0L, 0L))
                .build();
    }

    @Override
    public Page<DashboardListDto> getRecentRegistrations(int page, int size) {
        logger.info("Get dashboard list service method called.");

        try {
            Pageable pageRequest = PageRequest.of(page, size);
            Page<DashboardListDto> dashboardList = enrollmentRepo.getAllRecentEnrollments(pageRequest);

            return dashboardList != null ? dashboardList : Page.empty();
        } catch (Exception e) {
            logger.error("Error occurred in getRecentRegistrations service call", e);
        }

        return Page.empty();
    }
}
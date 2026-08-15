package com.sky.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {

    private long totalStudents;

    private long totalCourses;

    private Map.Entry<String, Long> topCourse;

    private Map.Entry<Long, Long> newRegistrations;
}

package com.sky.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardListDto {

    Long studentId;

    String fullName;

    String nameAcronym;

    String courseName;

    LocalDate enrolledAt;

    String duration;
}

package com.sky.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentViewDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Integer coursesCount;

    private BigDecimal totalFee;

    private List<EnrollmentCourseDto> courseJsonList;

}

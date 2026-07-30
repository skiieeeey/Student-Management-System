package com.sky.studentmanagement.dto.projections;

import java.math.BigDecimal;

public interface EnrollmentViewProjection {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    Integer getCoursesCount();
    BigDecimal getTotalFee();
    String getCourseJsonList();
}

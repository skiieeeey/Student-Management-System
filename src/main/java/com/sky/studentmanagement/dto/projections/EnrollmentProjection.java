package com.sky.studentmanagement.dto.projections;

import java.math.BigDecimal;

public interface EnrollmentProjection {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    Integer getCoursesCount();
    BigDecimal getTotalFee();
}

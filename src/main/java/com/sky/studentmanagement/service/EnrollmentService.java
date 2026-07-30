package com.sky.studentmanagement.service;

import com.sky.studentmanagement.dto.EnrollmentDto;
import com.sky.studentmanagement.dto.EnrollmentViewDto;
import org.springframework.data.domain.Page;

public interface EnrollmentService {
    Page<EnrollmentDto> getAllEnrollments(int page, int size);

    EnrollmentViewDto getEnrollmentById(Long id);
}

package com.sky.studentmanagement.service;

import com.sky.studentmanagement.dto.EnrollmentDto;
import com.sky.studentmanagement.dto.EnrollmentViewDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EnrollmentService {
    Page<EnrollmentDto> getAllEnrollments(int page, int size);

    EnrollmentViewDto getEnrollmentById(Long id);

    void enrollStudent(Long studentId, List<Long> courseIds);
}

package com.sky.studentmanagement.service.impl;

import com.sky.studentmanagement.dto.CourseDto;
import com.sky.studentmanagement.dto.EnrollmentCourseDto;
import com.sky.studentmanagement.dto.EnrollmentDto;
import com.sky.studentmanagement.dto.EnrollmentViewDto;
import com.sky.studentmanagement.dto.projections.EnrollmentViewProjection;
import com.sky.studentmanagement.repository.CourseRepo;
import com.sky.studentmanagement.repository.EnrollmentRepo;
import com.sky.studentmanagement.repository.StudentRepo;
import com.sky.studentmanagement.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    @Autowired
    private final EnrollmentRepo enrollmentRepo;

    @Autowired
    private final StudentRepo studentRepo;

    @Autowired
    private final CourseRepo courseRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EnrollmentServiceImpl(EnrollmentRepo enrollmentRepo, StudentRepo studentRepo, CourseRepo courseRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    @Override
    public Page<EnrollmentDto> getAllEnrollments(int page, int size) {
        logger.info("List of enrollments from: {}", page);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<EnrollmentDto> result =  enrollmentRepo.fetchActiveStudentSummaries(pageRequest)
                .map(e -> new EnrollmentDto(e.getId(), e.getFirstName(), e.getLastName(), e.getEmail(), e.getCoursesCount(), e.getTotalFee()));

        return result;
    }

    @Override
    public EnrollmentViewDto getEnrollmentById(Long id) {
        logger.info("View enrollment by ID service called for ID: {}", id);
        try {
            EnrollmentViewProjection projection = enrollmentRepo.findByStudentSummaryById(id);

            if (projection == null){
                throw new RuntimeException("No student found with ID: "+ id);
            }

            EnrollmentViewDto enrollment = EnrollmentViewDto.builder()
                    .id(projection.getId())
                    .firstName(projection.getFirstName())
                    .lastName(projection.getLastName())
                    .email(projection.getEmail())
                    .coursesCount(projection.getCoursesCount())
                    .totalFee(projection.getTotalFee())
                    .build();
            if(projection.getCourseJsonList() != null){
                List<EnrollmentCourseDto> courseList = objectMapper.readValue(
                        projection.getCourseJsonList(),
                        new TypeReference<List<EnrollmentCourseDto>>() {}
                );
                enrollment.setCourseJsonList(courseList);
            }else{
                enrollment.setCourseJsonList(List.of());
            }
            return enrollment;
        }catch(Exception e){
            logger.error("Error occurred at view enrollment by ID service for ID: {} "+e.getMessage(), id);
            throw new RuntimeException("Error processing student snapshot.", e);
        }
    }
}

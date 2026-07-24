package com.sky.studentmanagement.service.impl;

import com.sky.studentmanagement.dto.CourseDto;
import com.sky.studentmanagement.exception.CustomException;
import com.sky.studentmanagement.model.Courses;
import com.sky.studentmanagement.repository.CourseRepo;
import com.sky.studentmanagement.service.CourseService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseRepo courseRepo;

    @Override
    public void addCourse(CourseDto course) throws CustomException.AttributeException {
        logger.info("Add Course service called.");
        try{
            Courses newCourse = Courses.builder()
                            .courseName(course.getCourseName())
                                    .courseCode(course.getCourseCode())
                                            .duration(course.getDuration())
                                                    .fee(course.getFee())
                                                            .description(course.getDescription())
                                                                            .build();
            courseRepo.save(newCourse);
        }catch(Exception ex){
            logger.error("Error in Model Attribute.");
            throw new CustomException.AttributeException("Error occurred in the attributes.");
        }
    }

    @Override
    public Page<CourseDto> getAllCourses(int page, int size){
        logger.info("List of courses from: {}", page);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<CourseDto> result =  courseRepo.findByActiveTrue(pageRequest)
                .map(course -> new CourseDto(course.getId(), course.getCourseName(), course.getCourseCode(), course.getDuration(), course.getFee(), course.getDescription()));

        return result;
    }

    @Override
    public boolean existByCourseCode(String courseCode){
        logger.info("Course code duplication validation service called.");
        return courseRepo.existsByCourseCodeIgnoreCase(courseCode);
    }
}

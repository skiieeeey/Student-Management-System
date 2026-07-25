package com.sky.studentmanagement.service.impl;

import com.sky.studentmanagement.dto.CourseDto;
import com.sky.studentmanagement.dto.CourseModifyDto;
import com.sky.studentmanagement.exception.CustomException;
import com.sky.studentmanagement.model.Courses;
import com.sky.studentmanagement.repository.CourseRepo;
import com.sky.studentmanagement.service.CourseService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
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
                .map(course -> new CourseDto(course.getId(), course.getCourseName(), course.getCourseCode(), course.getDuration(), course.isActive(), course.getFee(), course.getDescription()));

        return result;
    }

    @Override
    public boolean existByCourseCode(String courseCode){
        logger.info("Course code duplication validation service called.");
        return courseRepo.existsByCourseCodeIgnoreCase(courseCode);
    }

    @Override
    public boolean existByCourseCodeAndIdNot(String courseCode, Long id) {
        logger.info("Course code duplication validation service for modify course called.");
        return courseRepo.existsByCourseCodeIgnoreCaseAndIdNot(courseCode, id);
    }

    @Override
    @Transactional
    @Modifying
    public CourseDto editCourseById(CourseModifyDto newCourse) {
        logger.info("Edit course service for ID : {} called", newCourse.getId());
        Courses course = courseRepo.findById(newCourse.getId())
                .orElseThrow(() -> new RuntimeException("No Course Found"));

        if(newCourse.isSpecifiedCourseName()){
            course.setCourseName(newCourse.getCourseName());
        }
        if(newCourse.isSpecifiedCourseCode()){
            course.setCourseCode(newCourse.getCourseCode());
        }
        if(newCourse.isSpecifiedDuration()){
            course.setDuration(newCourse.getDuration());
        }
        if(!newCourse.isActive()){
            course.setActive(false);
        }
        if(newCourse.isSpecifiedFee()){
            course.setFee(newCourse.getFee());
        }
        if(newCourse.isSpecifiedDescription()){
            course.setDescription(newCourse.getDescription());
        }

        courseRepo.save(course);

        return new CourseDto().builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .courseCode(course.getCourseCode())
                .duration(course.getDuration())
                .active(course.isActive())
                .fee(course.getFee())
                .description(course.getDescription())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long id){
        CourseDto course = courseRepo.findById(id)
                .map(c -> new CourseDto(c.getId(), c.getCourseName(), c.getCourseCode(), c.getDuration(), c.isActive() ,c.getFee(), c.getDescription()))
                .orElseThrow(() -> new RuntimeException("No Course Found"));

        return course;
    }
}

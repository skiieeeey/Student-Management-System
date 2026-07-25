package com.sky.studentmanagement.service;

import com.sky.studentmanagement.dto.CourseDto;
import com.sky.studentmanagement.dto.CourseModifyDto;
import com.sky.studentmanagement.exception.CustomException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {

    void addCourse(CourseDto course) throws CustomException.AttributeException;

    Page<CourseDto> getAllCourses(int page, int size);

    CourseDto getCourseById(Long id);

    boolean existByCourseCode(String courseCode);

    boolean existByCourseCodeAndIdNot(String courseCode, Long id);

    CourseDto editCourseById(CourseModifyDto course);
}
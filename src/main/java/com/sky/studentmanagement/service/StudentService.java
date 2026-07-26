package com.sky.studentmanagement.service;

import com.sky.studentmanagement.dto.StudentDto;
import com.sky.studentmanagement.dto.StudentModifyDto;
import com.sky.studentmanagement.exception.CustomException;
import org.springframework.data.domain.Page;

public interface StudentService {

    Page<StudentDto> getAllStudents(int page, int size);

    void addStudent(StudentDto student) throws CustomException.AttributeException;

    StudentDto getStudentById(Long id);

    boolean existByEmailAndPhoneAndIdNot(String email, String phone, Long id);

    StudentDto editStudentById(StudentModifyDto student);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);
}

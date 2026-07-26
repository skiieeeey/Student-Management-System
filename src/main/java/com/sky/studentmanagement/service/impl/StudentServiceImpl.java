package com.sky.studentmanagement.service.impl;

import com.sky.studentmanagement.dto.StudentDto;
import com.sky.studentmanagement.dto.StudentModifyDto;
import com.sky.studentmanagement.exception.CustomException;
import com.sky.studentmanagement.model.Students;
import com.sky.studentmanagement.repository.StudentRepo;
import com.sky.studentmanagement.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private final StudentRepo studentRepo;

    public StudentServiceImpl(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    @Override
    public Page<StudentDto> getAllStudents(int page, int size) {
        logger.info("List of students from: {}", page);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<StudentDto> result =  studentRepo.findByActiveTrue(pageRequest)
                .map(student -> new StudentDto(student.getId(), student.getFirstName(), student.getLastName(), student.getEmail(), student.getPhone(), student.isActive()));

        return result;
    }

    @Override
    public void addStudent(StudentDto student) throws CustomException.AttributeException {
        logger.info("Add Student service called.");
        try{
            Students newStudent = Students.builder()
                    .firstName(student.getFirstName())
                    .lastName(student.getLastName())
                    .email(student.getEmail())
                    .phone(student.getPhone())
                    .build();
            studentRepo.save(newStudent);
        }catch(Exception ex){
            logger.error("Error in Model Attribute.");
            throw new CustomException.AttributeException("Error occurred in the attributes.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long id) {
        StudentDto student = studentRepo.findById(id)
                .map(s -> new StudentDto(s.getId(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getPhone(), s.isActive()))
                .orElseThrow(() -> new RuntimeException("Student with given ID not found"));

        return student;
    }

    @Override
    public boolean existByEmailAndPhoneAndIdNot(String email, String phone, Long id) {
        logger.info("Email and Phone duplication validation service for modify student called.");
        return studentRepo.existsByEmailAndPhoneIgnoreCaseAndIdNot(email, phone, id);
    }

    @Override
    @Transactional
    @Modifying
    public StudentDto editStudentById(StudentModifyDto newStudent) {
        logger.info("Edit student service for ID : {} called", newStudent.getId());
        Students student = studentRepo.findById(newStudent.getId())
                .orElseThrow(() -> new RuntimeException("No Student Found"));

        if(newStudent.getEmail() != null){
            student.setEmail(newStudent.getEmail());
        }
        if(newStudent.getPhone() != null){
            student.setPhone(newStudent.getPhone());
        }
        if(!newStudent.isActive()){
            student.setActive(false);
        }

        studentRepo.save(student);

        return StudentDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .active(student.isActive())
                .build();
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        logger.info("Email duplication validation service for modify student called.");
        return studentRepo.existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    @Override
    public boolean existsByPhoneAndIdNot(String phone, Long id) {
        logger.info("Phone duplication validation service for modify student called.");
        return studentRepo.existsByPhoneIgnoreCaseAndIdNot(phone, id);
    }
}
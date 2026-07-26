package com.sky.studentmanagement.controller;

import com.sky.studentmanagement.dto.CourseDto;
import com.sky.studentmanagement.dto.CourseModifyDto;
import com.sky.studentmanagement.dto.StudentDto;
import com.sky.studentmanagement.dto.StudentModifyDto;
import com.sky.studentmanagement.exception.CustomException;
import com.sky.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/list")
    public String showStudentsList(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   Model model){
        log.info("Get student/list - list student page request received.");

        Page<StudentDto> allStudents = studentService.getAllStudents(page, size);
        model.addAttribute("students",  allStudents);

        return "students";
    }

    @GetMapping("/add")
    public String showCreateCourse(Model model){
        log.info("Get /student/add - showing add course page.");
        model.addAttribute("student", new StudentDto());
        return "add-student";
    }

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute(name = "student") StudentDto student,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) throws CustomException.AttributeException {
        log.info("Post /student/add - add student request received.");
        if(result.hasErrors()){
            log.info("Post /student/add - page return due to validation error.");
            result.getFieldErrors().forEach(error -> {
                log.error("Field: " + error.getField() + " Rejected: " + error.getRejectedValue() + " Error: " + error.getDefaultMessage());
            });
            return "add-student";
        }

//        if(studentService.existByEmailAndPhoneAndIdNot(student.getEmail(), student.getPhone(), student.getId())){
//            log.info("Put /course/{}/edit - page return because student exists with the given email and phone.", student.getId());
//            result.rejectValue("email", "student.email.exists");
//            result.rejectValue("phone", "student.phone.exists");
//            return "add-student";
//        }

        if(studentService.existsByEmailAndIdNot(student.getEmail(), student.getId())){
            log.info("Put /course/{}/edit - page return because student exists with the given email.", student.getId());
            result.rejectValue("email", "student.email.exists");
            return "add-student";
        }

        if(studentService.existsByPhoneAndIdNot(student.getPhone(), student.getId())){
            log.info("Put /course/{}/edit - page return because student exists with the given phone.", student.getId());
            student.setId(student.getId());
            result.rejectValue("phone", "student.phone.exists");
            return "add-student";
        }


        studentService.addStudent(student);
        redirectAttributes.addFlashAttribute("message" , "Student added successfully!");
        return "redirect:/student/list";
    }

    @GetMapping("/{id}")
    public String getStudent(@PathVariable Long id,
                             Model model){
        log.info("Get /student/{} - view student request received for id: ", id);
        StudentDto student = studentService.getStudentById(id);
        model.addAttribute("student", student);

        return "view-student";
    }

    @GetMapping("/{id}/edit")
    public String editCourse(@PathVariable Long id,
                             Model model){
        log.info("Get student/{}/edit - view request to edit the student received.", id);
        StudentDto student = studentService.getStudentById(id);
        model.addAttribute("student", student);

        return "edit-student";
    }

    @PutMapping("/{id}/edit")
    public String editCourseById(@PathVariable Long id,
                                 @ModelAttribute(name = "student") StudentModifyDto student,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model){

        log.info("Put /student/{id}/edit - edit request to the student received.");

        if(result.hasErrors()){
            log.info("Put /student/{}/edit - page return due to validation error.", id);
            student.setId(id);
            return "edit-student";
        }

//        if(studentService.existByEmailAndPhoneAndIdNot(student.getEmail(), student.getPhone(), id)){
//            log.info("Put /course/{}/edit - page return because student exists with the given email and phone.", id);
//            student.setId(id);
//            result.rejectValue("email", "student.email.exists");
//            result.rejectValue("phone", "student.phone.exists");
//            return "edit-student";
//        }

        if(studentService.existsByEmailAndIdNot(student.getEmail(), id)){
            log.info("Put /course/{}/edit - page return because student exists with the given email.", id);
            student.setId(id);
            result.rejectValue("email", "student.email.exists");
            return "edit-student";
        }

        if(studentService.existsByPhoneAndIdNot(student.getPhone(), id)){
            log.info("Put /course/{}/edit - page return because student exists with the given phone.", id);
            student.setId(id);
            result.rejectValue("phone", "student.phone.exists");
            return "edit-student";
        }

        student.setId(id);
        StudentDto newStudent = studentService.editStudentById(student);

        if(!newStudent.isActive()){
            log.info("Student disabled: " + id);
            redirectAttributes.addFlashAttribute("message", "Student disabled successfully.");
            return "redirect:/student/list";
        }

        redirectAttributes.addFlashAttribute("message", "Student modified successfully.");
        log.info("Student modified successfully with id: "+id);

        return "redirect:/student/list";
    }
}

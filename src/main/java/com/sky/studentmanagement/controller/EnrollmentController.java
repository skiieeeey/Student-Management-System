package com.sky.studentmanagement.controller;

import com.sky.studentmanagement.dto.EnrollmentDto;
import com.sky.studentmanagement.dto.EnrollmentViewDto;
import com.sky.studentmanagement.service.CourseService;
import com.sky.studentmanagement.service.EnrollmentService;
import com.sky.studentmanagement.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private final EnrollmentService enrollmentService;

    @Autowired
    private final CourseService courseService;

    @Autowired
    private final StudentService studentService;

    public EnrollmentController(EnrollmentService enrollmentService, CourseService courseService, StudentService studentService) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.studentService = studentService;
    }


    @GetMapping("/list")
    public String showEnrollmentsList(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      Model model){
        log.info("Get enrollment/list - list enrollment page request received.");

        Page<EnrollmentDto> allEnrollments = enrollmentService.getAllEnrollments(page, size);
        model.addAttribute("enrollments",  allEnrollments);

        return "enrolled-students";
    }

    @GetMapping("/add")
    public String showEnrollStudent(Model model){
        log.info("Get /enrollment/add - showing add enrollment page.");
        model.addAttribute("courses", courseService.getAllCoursesList());
        model.addAttribute("students", studentService.getAllStudentsList());
        return "enroll-course";
    }

    @PostMapping("/add")
    public String addEnrollment(
                            @RequestParam("studentId") Long studentId,
                            @RequestParam(value = "courseIds", required = false) List<Long> courseIds,
                            RedirectAttributes redirectAttributes) {
        log.info("Post /enrollment/add - add enrollment request received.");

        if(courseIds == null || courseIds.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Please select at least one course to enroll.");
            return "redirect:/enrollment/add";
        }

        try{
            enrollmentService.enrollStudent(studentId, courseIds);
            redirectAttributes.addFlashAttribute("message", "Student successfully enrolled in courses!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enrollment/list";
    }

    @GetMapping("/{id}")
    public String getEnrollment(@PathVariable Long id,
                                Model model){
        log.info("Get /enrollment/{} - view enrollment request received for id: ", id);
        EnrollmentViewDto enrollment = enrollmentService.getEnrollmentById(id);
        model.addAttribute("enrollment", enrollment);

        return "enrollment-details";
    }
}

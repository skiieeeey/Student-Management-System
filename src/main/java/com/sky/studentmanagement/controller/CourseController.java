package com.sky.studentmanagement.controller;

import com.sky.studentmanagement.dto.CourseDto;
import com.sky.studentmanagement.exception.CustomException;
import com.sky.studentmanagement.service.CourseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/course")
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/add")
    public String showCreateCourse(Model model){
        log.info("Get /course/add - showing add course page.");
        model.addAttribute("course", new CourseDto());
        return "add-course";
    }

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute(name = "course") CourseDto course,
                            RedirectAttributes redirectAttributes,
                            BindingResult result) throws CustomException.AttributeException {
        log.info("Post /course/add - add course request received.");
        if(result.hasErrors()){
            log.info("Post /course/add - page return due to validation error.");
            return "add-course";
        }

        if(courseService.existByCourseCode(course.getCourseCode())){
            log.info("Post /course/add - page return due to duplicate code error.");
            result.rejectValue("courseCode", "course.code.exists");
            return "add-course";
        }


        courseService.addCourse(course);
        redirectAttributes.addFlashAttribute("message" , "Course added successfully!");
        return "redirect:/course/list";
    }

    @GetMapping("/list")
    public String listCourse(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             Model model){
        log.info("Get course/list - list course page request received.");

        Page<CourseDto> allCourses = courseService.getAllCourses(page, size);
        model.addAttribute("courses",  allCourses);

        return "courses";
    }

}

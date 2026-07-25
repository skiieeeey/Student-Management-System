package com.sky.studentmanagement.controller;

import com.sky.studentmanagement.dto.CourseDto;
import com.sky.studentmanagement.dto.CourseModifyDto;
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

    @GetMapping("/{id}")
    public String getCourseById(@PathVariable Long id,
                                Model model){
        log.info("Get course/{id} - view request to the course received.");
        CourseDto course = courseService.getCourseById(id);
        model.addAttribute("course", course);

        return "view-course";
    }

    @GetMapping("/{id}/edit")
    public String editCourse(@PathVariable Long id,
                             Model model){
        log.info("Get course/{id}/edit - view request to edit the course received.");
        CourseDto course = courseService.getCourseById(id);
        model.addAttribute("course", course);

        return "edit-course";
    }

    @PutMapping("/{id}/edit")
    public String editCourseById(@PathVariable Long id,
                                 @ModelAttribute(name = "course") CourseModifyDto course,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model){

        log.info("Put course/{id}/edit - edit request to the course received.");

        if(result.hasErrors()){
            log.info("Put /course/{}/edit - page return due to validation error.", id);
            course.setId(id);
            return "edit-course";
        }

        if(courseService.existByCourseCodeAndIdNot(course.getCourseCode(), id)){
            log.info("Put /course/{}/edit - page return due to duplicate code error.", id);
            course.setId(id);
            result.rejectValue("courseCode", "course.code.exists");
            return "edit-course";
        }

        course.setId(id);
        CourseDto newCourse = courseService.editCourseById(course);

        if(!newCourse.isActive()){
            log.info("Course disabled: " + id);
            redirectAttributes.addFlashAttribute("message", "Course disabled successfully.");
            return "redirect:/course/list";
        }

        redirectAttributes.addFlashAttribute("message", "Course modified successfully.");
        log.info("Course modified successfully with id: "+id);

        return "redirect:/course/list";
    }

}

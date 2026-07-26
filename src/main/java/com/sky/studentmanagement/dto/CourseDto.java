package com.sky.studentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    
    private Long id;

    @NotBlank(message = "Course name is required.")
    @Size(max = 150, message = "Max of 150 characters allowed.")
    private String courseName;

    @NotBlank(message = "Course code is required.")
    private String courseCode;

    @NotBlank(message = "Course duration is required.")
    private String duration;

    private boolean active;

    @NotNull(message = "Course fee is required.")
    private BigDecimal fee;

    @Size(max = 500, message = "Max of 500 characters allowed.")
    private String description;


    public boolean isSpecifiedCourseName(){
        return this.courseName != null;
    }

    public boolean isSpecifiedCourseCode(){
        return this.courseCode != null;
    }

    public boolean isSpecifiedDuration(){
        return this.duration != null;
    }

    public boolean isSpecifiedFee(){
        return this.fee != null;
    }

    public boolean isSpecifiedDescription(){
        return this.description != null;
    }
}

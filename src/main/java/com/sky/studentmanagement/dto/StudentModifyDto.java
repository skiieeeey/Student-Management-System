package com.sky.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentModifyDto {

    private Long id;

    @Email(message = "Invalid Email")
    private String email;

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number. Must be 10 digits starting with 6-9"
    )
    private String phone;

    private boolean active;
}

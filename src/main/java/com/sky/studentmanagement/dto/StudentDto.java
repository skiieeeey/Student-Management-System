package com.sky.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number. Must be 10 digits starting with 6-9"
    )
    private String phone;

    private boolean active;


    public boolean isSpecifiedFirstName(){
        return this.firstName != null;
    }

    public boolean isSpecifiedLastName(){
        return this.lastName != null;
    }

    public boolean isSpecifiedEmail(){
        return this.email != null;
    }

    public boolean isSpecifiedPhone(){
        return this.phone != null;
    }

}

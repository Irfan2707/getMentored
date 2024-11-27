package com.nineleaps.authentication.jwt.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRegistrationDTO {
    @NotNull(message = "User email is required")
    @Email(message = "Invalid email format")
    private String userMail;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Pattern(regexp = "\\+\\d{2} \\d{10}", message = "Phone number should be in the format: +91 9025756890")
    private String phoneNumber;

}
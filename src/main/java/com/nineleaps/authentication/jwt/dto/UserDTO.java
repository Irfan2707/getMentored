package com.nineleaps.authentication.jwt.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDTO {
    private Long id;
    @NotNull(message = "User name is required")
    @Size(min = 1, max = 255, message = "User name must be between 1 and 255 characters")
    private String userName;

    @NotNull(message = "User email is required")
    @Email(message = "Invalid email format")
    private String userMail;

    @Pattern(regexp = "\\+\\d{2} \\d{10}", message = "Phone number should be in the format: +91 9025756890")
    private String phoneNumber;

    private byte[] profileImage;

    @Size(max = 255, message = "Expertise must be at most 255 characters")
    private String expertise;

    @Size(max = 255, message = "Location must be at most 255 characters")
    private String location;

    @Size(max = 500, message = "Bio must be at most 500 characters")
    private String bio;

    @Size(max = 255, message = "Industry must be at most 255 characters")
    private String industry;

    @Size(max = 255, message = "Mentoring required for must be at most 255 characters")
    private String mentoringRequiredFor;

    @NotNull(message = "Charge per hour is required")
    private double chargePerHour;

    private Double averageRating;


}
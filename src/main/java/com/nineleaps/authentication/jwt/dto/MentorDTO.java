package com.nineleaps.authentication.jwt.dto;

import com.nineleaps.authentication.jwt.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorDTO {
    private Long id;
    private Set<UserRole> roles;
    @NotEmpty
    @Size(max = 255)
    private String userName;

    @NotEmpty
    @Email
    private String userMail;

    @NotEmpty
    @Size(max = 15)
    private String phoneNumber;

    private byte[] profileImage;

    @Size(max = 255)
    private String expertise;

    @Size(max = 255)
    private String location;

    @Size(max = 255)
    private String bio;

    @Size(max = 255)
    private String industry;

    @NotNull
    private double chargePerHour;

    private Double averageMentorRating;
    private Double averageMenteeRating;


}

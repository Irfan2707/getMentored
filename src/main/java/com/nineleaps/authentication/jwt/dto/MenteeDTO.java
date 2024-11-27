package com.nineleaps.authentication.jwt.dto;

import com.nineleaps.authentication.jwt.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenteeDTO {
    private Long id;
    private Set<UserRole> roles;
    @NotBlank
    private String userName;

    @NotBlank
    @Email
    private String userMail;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank
    private String location;

    @NotBlank
    private String bio;

    @NotBlank
    private String mentoringRequiredFor;

}

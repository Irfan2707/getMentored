package com.nineleaps.authentication.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SSOUserDto {
    @NotNull
    private Long id;

    @NotBlank
    @Email
    private String userMail;

    @NotBlank
    @Size(max = 26, min = 4)
    private String userName;


}

	
	



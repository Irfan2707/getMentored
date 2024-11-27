package com.nineleaps.authentication.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class SmsPojo {
    @Pattern(regexp = "\\+\\d{2} \\d{10}", message = "Phone number should be in the format: +91 9025756890")
    private String phoneNumber;

}

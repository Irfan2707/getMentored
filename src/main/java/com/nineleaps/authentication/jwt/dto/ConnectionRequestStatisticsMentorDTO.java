package com.nineleaps.authentication.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequestStatisticsMentorDTO {

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int received;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int accepted;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int rejected;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int pending;


}



package com.nineleaps.authentication.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EngagementStatisticsDTO {
    @NotNull
    @Min(0)
    private Long totalEngagements;

    @NotNull
    @Min(0)
    private Long totalHours;

    @NotNull
    @Positive
    private Double averageHours;


}


package com.nineleaps.authentication.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserStatsDTO {

    @NotNull(message = "Total users count is required")
    @Min(value = 0, message = "Total users count must be a non-negative number")
    private Long totalUsers;

    @NotNull(message = "Mentees count is required")
    @Min(value = 0, message = "Mentees count must be a non-negative number")
    private Long menteesCount;

    @NotNull(message = "Mentors count is required")
    @Min(value = 0, message = "Mentors count must be a non-negative number")
    private Long mentorsCount;


}


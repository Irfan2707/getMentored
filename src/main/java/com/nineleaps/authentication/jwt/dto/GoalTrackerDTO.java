package com.nineleaps.authentication.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalTrackerDTO {
    private Long id;
    @NotNull
    private Long engagementId;

    @NotBlank
    private String description;

    @NotNull
    private Long userId;

    @NotNull
    private LocalDateTime goalTrackerStartTime;

}

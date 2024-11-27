package com.nineleaps.authentication.jwt.dto;

import com.nineleaps.authentication.jwt.enums.EngStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor

public class EngagementStatusDTO {
    @NotNull
    private Long engagementStatusId;

    @NotNull
    private Long engagementId;

    @NotNull
    private EngStatus mentorEngStatus;

    @NotNull
    private EngStatus menteeEngStatus;

    @PastOrPresent
    private LocalDateTime mentorStatusTimestamp;

    @PastOrPresent
    private LocalDateTime menteeStatusTimestamp;

    @FutureOrPresent
    private LocalDateTime completedEngStatusTimestamp;


}
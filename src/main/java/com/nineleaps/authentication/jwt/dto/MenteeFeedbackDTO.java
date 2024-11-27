package com.nineleaps.authentication.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MenteeFeedbackDTO {
    private Long id;
    @NotNull
    @Min(1)
    @Max(5)
    private Double mentorRating;

    @Size(max = 255)
    private String mentorFeedback;

    @NotNull
    @Min(1)
    @Max(5)
    private Double engagementRating;

    @Size(max = 255)
    private String engagementFeedback;

    @Size(max = 255)
    private String feedbackFromUserName;

    @PastOrPresent
    private LocalDateTime createdTime;


}

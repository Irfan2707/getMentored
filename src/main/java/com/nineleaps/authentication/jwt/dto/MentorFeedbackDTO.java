package com.nineleaps.authentication.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MentorFeedbackDTO {
    private Long id;
    @NotNull
    private Double menteeRating;

    @Size(max = 255)
    private String menteeFeedback;

    @NotNull
    private Double engagementRating;

    @Size(max = 255)
    private String engagementFeedback;

    @Size(max = 255)
    private String feedbackFromUserName;

    private LocalDateTime createdTime;


}
		
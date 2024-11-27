package com.nineleaps.authentication.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackAnalyticsDTO {
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "5.0", inclusive = true)
    private Double averageEngagementRating;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "5.0", inclusive = true)
    private Double averageMenteeRating;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "5.0", inclusive = true)
    private Double averageMentorRating;

    @Positive
    private Long positiveFeedbackCount;

    @Positive
    private Long negativeFeedbackCount;

    @Positive
    private Long neutralFeedbackCount;


}

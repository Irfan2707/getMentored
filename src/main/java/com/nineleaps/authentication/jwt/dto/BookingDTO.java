package com.nineleaps.authentication.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long id;

    @NotNull(message = "Slot ID must not be null")
    private Long slotId;

    @NotNull(message = "Mentee ID must not be null")
    private Long menteeId;

    @NotNull(message = "Mentor ID must not be null")
    private Long mentorId;

    @NotNull(message = "Booking date and time must not be null")
    private LocalDateTime bookingDateTime;

    @NotNull(message = "Engagement ID must not be null")
    private Long engagementId;

    @Min(value = 1, message = "Number of hours must be at least 1")
    private Integer noOfHours;

    @Size(max = 255, message = "Mentee username must not exceed 255 characters")
    private String menteeUsername;

    @Size(max = 255, message = "Mentor username must not exceed 255 characters")
    private String mentorUsername;
}

    
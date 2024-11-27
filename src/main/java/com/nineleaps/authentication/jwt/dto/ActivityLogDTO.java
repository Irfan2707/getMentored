package com.nineleaps.authentication.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogDTO {

    private Long id;


    @NotNull(message = "Activity type cannot be null")
    @Size(min = 1, max = 255, message = "Activity type must be between 1 and 255 characters")
    private String activityType;

    @NotNull(message = "Activity time cannot be null")
    private LocalDateTime activityTime;

    @NotNull(message = "User name cannot be null")
    @Size(min = 1, max = 50, message = "User name must be between 1 and 50 characters")
    private String userName;

    @NotNull(message = "Checklist ID cannot be null")
    private Long checklistId;

    @Size(max = 255, message = "Item description must be less than or equal to 255 characters")
    private String itemDescription;

}

package com.nineleaps.authentication.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlotStatisticsDTOTotal {

    @NotNull
    @Min(0)
    private int totalSlots;
    @NotNull
    @Min(0)
    private int bookedSlots;
    @NotNull
    @Min(0)
    private int pendingSlots;

}


package com.nineleaps.authentication.jwt.dto;

import com.nineleaps.authentication.jwt.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter

public class SlotDTO {

    private Long id;
    @NotNull
    @FutureOrPresent
    private LocalDateTime startDateTime;
    @NotNull
    @Future
    private LocalDateTime endDateTime;
    @NotNull
    private Long mentorId;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;


}
		
		
		  
		  
				
				
			
		

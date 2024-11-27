package com.nineleaps.authentication.jwt.dto;

import com.nineleaps.authentication.jwt.entity.Engagement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class CreateEngagementRequestDTO {

    private Long id;
    private ConnectionRequestDto connectionRequest;
    private LocalDateTime startTime;
    private int durationHours;


    public static CreateEngagementRequestDTO fromEngagement(Engagement engagement) {
        CreateEngagementRequestDTO dto = new CreateEngagementRequestDTO();

        dto.setId(engagement.getId());
        dto.setStartTime(engagement.getStartTime().toString()); // Convert LocalDateTime to String
        dto.setDurationHours(engagement.getDurationHours());
        dto.setConnectionRequest(ConnectionRequestDto.fromConnectionRequest(engagement.getConnectionRequest()));
        return dto;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        // Define the desired date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // Parse the string and convert it to a LocalDateTime object
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }


    public Engagement toEngagement() {
        Engagement engagement = new Engagement();
        engagement.setStartTime(this.startTime);
        engagement.setDurationHours(this.durationHours);
        return engagement;
    }
}

	
	
	
	


package com.nineleaps.authentication.jwt.dto;

import com.nineleaps.authentication.jwt.entity.ConnectionRequest;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ConnectionRequestDto {
    private Long id;
    private Long menteeId;
    private Long mentorId;
    private String message;
    private ConnectionRequestStatus status;
    private LocalDateTime requestTime;
    private LocalDateTime acceptanceTime;
    private LocalDateTime rejectionTime;
    private List<Long> recommendedMentors;


    public ConnectionRequestDto(ConnectionRequest connectionRequest) {
        this.id = connectionRequest.getId();
        this.menteeId = connectionRequest.getMentee().getId();
        this.mentorId = connectionRequest.getMentor().getId();
        this.message = connectionRequest.getMessage();
        this.status = connectionRequest.getStatus();
        this.requestTime = connectionRequest.getRequestTime();
        this.acceptanceTime = connectionRequest.getAcceptanceTime();
        this.rejectionTime = connectionRequest.getRejectionTime();
    }

    public static ConnectionRequestDto fromConnectionRequest(ConnectionRequest connectionRequest) {
        return new ConnectionRequestDto(connectionRequest);
    }
}

package com.nineleaps.authentication.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequestStatisticsReportDTO {

    @NotNull
    @Min(0)
    private Long totalRequests;

    @NotNull
    @Min(0)
    private Long acceptedRequests;

    @NotNull
    @Min(0)
    private Long rejectedRequests;

    @NotNull
    @Min(0)
    private Long pendingRequests;


}
	

package com.nineleaps.authentication.jwt.service.interfaces;


import com.nineleaps.authentication.jwt.dto.CreateEngagementRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IEngagementService {

    ResponseEntity<CreateEngagementRequestDTO> createEngagement(CreateEngagementRequestDTO createEngagementRequest);

    List<Map<String, Object>> getEngagementDetailsByUserId(Long userId);
}

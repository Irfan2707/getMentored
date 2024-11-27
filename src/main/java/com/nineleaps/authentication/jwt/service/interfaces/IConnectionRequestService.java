package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.controllerexceptions.ConnectionRequestNotFoundException;
import com.nineleaps.authentication.jwt.controllerexceptions.ConnectionRequestProcessingException;
import com.nineleaps.authentication.jwt.controllerexceptions.EmailNotRetrievedException;
import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.ConnectionRequestDto;
import com.nineleaps.authentication.jwt.dto.UserDTO;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;

import java.util.List;
import java.util.Map;

public interface IConnectionRequestService {

    ConnectionRequestDto sendConnectionRequest(Long mentorId, Long menteeId, String message) throws ConnectionRequestProcessingException;

    ConnectionRequestDto acceptConnectionRequest(Long connectionRequestId, Long mentorId, Long menteeId) throws ConnectionRequestNotFoundException, EmailNotRetrievedException;

    ConnectionRequestDto rejectConnectionRequest(Long connectionRequestId, Long mentorId, Long menteeId);

    List<ConnectionRequestDto> getAllConnectionRequestsByUserId(Long userId, int page, int size);


    public List<Object[]> findConnectionsByMenteeId(Long menteeId);

    ConnectionRequestDto recommendMentors(Long connectionRequestId, List<Long> recommendedMentorIds) throws ResourceNotFoundException;


    List<UserDTO> getUsersWithAcceptedConnectionAndAverageRating(Long userId);

    ConnectionRequestStatus getConnectionStatus(Long menteeId, Long mentorId);

    List<Map<String, Object>> getConnectionDetailsByUserId(Long userId);

    List<UserDTO> getRecommendedMentorsByMenteeId(Long menteeId);
}







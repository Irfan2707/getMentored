package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.ConnectionRequestDto;
import com.nineleaps.authentication.jwt.dto.UserDTO;
import com.nineleaps.authentication.jwt.entity.ConnectionRequest;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.exception.DuplicateConnectionRequestException;
import com.nineleaps.authentication.jwt.repository.ConnectionRequestRepo;
import com.nineleaps.authentication.jwt.repository.FeedbackRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.interfaces.IConnectionRequestService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConnectionRequestServiceImpl implements IConnectionRequestService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final ConnectionRequestRepo connectionRequestRepo;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private FeedbackRepository feedbackRepository;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionRequestServiceImpl.class);

    @Override
    public ConnectionRequestDto acceptConnectionRequest(Long connectionRequestId, Long mentorId, Long menteeId) {
        logger.info("Accepting connection request with ID: {} for mentor ID: {} and mentee ID: {}", connectionRequestId, mentorId, menteeId);

        Optional<ConnectionRequest> connectionRequestOpt = connectionRequestRepo.findById(connectionRequestId);

        if (!connectionRequestOpt.isPresent()) {
            logger.error("Connection request with ID {} is not found", connectionRequestId);
            throw new IllegalArgumentException("Connection request is not found");
        }

        ConnectionRequest connectionRequest = connectionRequestOpt.get();

        if (!Objects.equals(connectionRequest.getMentee().getId(), menteeId)) {
            logger.error("Mentee ID {} does not match the request's mentee ID", menteeId);
            throw new IllegalArgumentException("Mentee ID does not match");
        }

        connectionRequest.setStatus(ConnectionRequestStatus.ACCEPTED);
        connectionRequest.setAcceptanceTime(LocalDateTime.now());

        ConnectionRequest savedConnectionRequest = connectionRequestRepo.save(connectionRequest);
        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto(savedConnectionRequest);

        String menteeEmail = userServiceImpl.getMenteeEmailById(menteeId);
        String subject = "Acceptance Notification";
        String mentorName = savedConnectionRequest.getMentor().getUserName();
        String content = "Your request has been accepted by: " + mentorName;

        emailService.sendEmail(subject, content, menteeEmail);

        logger.info("Connection request with ID {} accepted successfully", connectionRequestId);

        return connectionRequestDto;
    }

    @Override
    public ConnectionRequestDto rejectConnectionRequest(Long connectionRequestId, Long mentorId, Long menteeId) {
        logger.info("Rejecting connection request with ID: {} for mentor ID: {} and mentee ID: {}", connectionRequestId, mentorId, menteeId);

        Optional<ConnectionRequest> connectionRequestOpt = connectionRequestRepo.findById(connectionRequestId);

        if (!connectionRequestOpt.isPresent()) {
            logger.error("Connection request with ID {} is not found", connectionRequestId);
            throw new IllegalArgumentException("Connection request not found");
        }

        ConnectionRequest connectionRequest = connectionRequestOpt.get();

        if (!connectionRequest.getMentor().getId().equals(mentorId)) {
            logger.error("Mentor ID {} does not match the request's mentor ID", mentorId);
            throw new IllegalArgumentException("Mentor ID does not match");
        }

        connectionRequest.setStatus(ConnectionRequestStatus.REJECTED);
        connectionRequest.setRejectionTime(LocalDateTime.now());

        ConnectionRequest savedConnectionRequest = connectionRequestRepo.save(connectionRequest);
        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto(savedConnectionRequest);

        logger.info("Connection request with ID {} rejected successfully", connectionRequestId);

        return connectionRequestDto;
    }


    @Override
    public ConnectionRequestDto sendConnectionRequest(Long mentorId, Long menteeId, String message) {
        logger.info("Sending connection request from mentor ID: {} to mentee ID: {}", mentorId, menteeId);

        User mentor = userRepository.findById(mentorId).orElseThrow();
        User mentee = userRepository.findById(menteeId).orElseThrow();

        ConnectionRequest connectionRequest = connectionRequestRepo.findByMentorAndMentee(mentor, mentee);

        if (connectionRequest != null && connectionRequest.getStatus() == ConnectionRequestStatus.REJECTED) {
            // Connection request previously rejected, update the status to "Pending"
            connectionRequest.setStatus(ConnectionRequestStatus.PENDING);
        } else if (connectionRequestRepo.existsByMentorAndMentee(mentor, mentee)) {
            logger.error("Duplicate connection request found from mentor ID: {} to mentee ID: {}", mentorId, menteeId);
            throw new DuplicateConnectionRequestException("A connection request already exists.");
        } else {
            connectionRequest = new ConnectionRequest();
            connectionRequest.setMentor(mentor);
            connectionRequest.setMentee(mentee);
            connectionRequest.setStatus(ConnectionRequestStatus.PENDING);
        }

        connectionRequest.setMessage(message);
        connectionRequest.setRequestTime(LocalDateTime.now());
        connectionRequestRepo.save(connectionRequest);

        return convertToDTO(connectionRequest);
    }


    @Override
    public List<ConnectionRequestDto> getAllConnectionRequestsByUserId(Long userId, int page, int size) {
        logger.info("Fetching all connection requests for user with ID: {}", userId);

        Pageable pageable = PageRequest.of(page, size);
        Page<ConnectionRequest> connectionRequestsPage = connectionRequestRepo.findAllByMenteeIdOrMentorId(userId, userId, pageable);

        List<ConnectionRequestDto> connectionRequestDto = new ArrayList<>();
        for (ConnectionRequest connectionRequest : connectionRequestsPage) {
            connectionRequestDto.add(mapToConnectionRequestDto(connectionRequest));
        }

        logger.info("Fetched {} connection requests for user with ID: {}", connectionRequestsPage.getTotalElements(), userId);

        return connectionRequestDto;
    }

    public ConnectionRequestDto mapToConnectionRequestDto(ConnectionRequest connectionRequest) {

        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto();
        connectionRequestDto.setId(connectionRequest.getId());
        connectionRequestDto.setMentorId(connectionRequest.getMentor().getId());
        connectionRequestDto.setMenteeId(connectionRequest.getMentee().getId());
        connectionRequestDto.setMessage(connectionRequest.getMessage());
        connectionRequestDto.setStatus(connectionRequest.getStatus());
        connectionRequestDto.setRequestTime(connectionRequest.getRequestTime());
        connectionRequestDto.setAcceptanceTime(connectionRequest.getAcceptanceTime());
        connectionRequestDto.setRejectionTime(connectionRequest.getRejectionTime());
        return connectionRequestDto;
    }

    @Override
    public List<Object[]> findConnectionsByMenteeId(Long menteeId) {
        logger.info("Finding connections by mentee ID: {}", menteeId);
        List<Object[]> connections = connectionRequestRepo.findConnectionsByMenteeId(menteeId);
        logger.info("Found {} connections by mentee ID: {}", connections.size(), menteeId);
        return connections;
    }


    public ConnectionRequestDto convertToDTO(ConnectionRequest connectionRequest) {
        ConnectionRequestDto connectionRequestDTO = new ConnectionRequestDto();
        connectionRequestDTO.setId(connectionRequest.getId());
        connectionRequestDTO.setMenteeId(connectionRequest.getMentee().getId());
        connectionRequestDTO.setMentorId(connectionRequest.getMentor().getId());
        connectionRequestDTO.setMessage(connectionRequest.getMessage());
        connectionRequestDTO.setStatus(connectionRequest.getStatus());
        connectionRequestDTO.setRequestTime(connectionRequest.getRequestTime());
        connectionRequestDTO.setAcceptanceTime(connectionRequest.getAcceptanceTime());
        connectionRequestDTO.setRejectionTime(connectionRequest.getRejectionTime());
        return connectionRequestDTO;
    }


    @Override
    public ConnectionRequestDto recommendMentors(Long connectionRequestId, List<Long> recommendedMentorIds) throws ResourceNotFoundException {
        logger.info("Recommend mentors for connection request with ID: {}", connectionRequestId);

        Optional<ConnectionRequest> optionalConnectionRequest = connectionRequestRepo.findById(connectionRequestId);

        if (optionalConnectionRequest.isPresent()) {
            ConnectionRequest connectionRequest = optionalConnectionRequest.get();
            connectionRequest.setRecommendedMentors(recommendedMentorIds);
            connectionRequestRepo.save(connectionRequest);

            ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto(connectionRequest);
            connectionRequestDto.setRecommendedMentors(recommendedMentorIds);

            logger.info("Mentor recommendations saved for connection request with ID: {}", connectionRequestId);

            return connectionRequestDto;
        } else {
            logger.error("Connection request with ID {} not found", connectionRequestId);
            throw new ResourceNotFoundException("Connection request not found");
        }
    }


    @Override
    public List<UserDTO> getRecommendedMentorsByMenteeId(Long menteeId) {
        logger.info("Getting recommended mentors for mentee with ID: {}", menteeId);

        List<ConnectionRequest> connectionRequests = connectionRequestRepo.findByMenteeId(menteeId);

        List<Long> mentorIds = connectionRequests.stream()
                .filter(request -> {
                    ConnectionRequestStatus status = getConnectionStatus(menteeId, request.getMentor().getId());
                    boolean filterResult = status == null || (status != ConnectionRequestStatus.PENDING && status != ConnectionRequestStatus.ACCEPTED);
                    logger.debug("Mentor ID: {}, Status: {}, Filtered: {}", request.getMentor().getId(), status, filterResult);
                    return filterResult;
                })
                .flatMap(request -> request.getRecommendedMentors().stream())
                .distinct()
                .filter(mentorId -> {
                    ConnectionRequestStatus status = getConnectionStatus(menteeId, mentorId);
                    return status != ConnectionRequestStatus.PENDING && status != ConnectionRequestStatus.ACCEPTED;
                })
                .collect(Collectors.toList());

        List<UserDTO> recommendedMentors = mentorIds.stream()
                .map(mentorId -> userRepository.findById(mentorId).orElse(null))
                .filter(Objects::nonNull)
                .map(this::convertToUserDTO) // Convert User entities to UserDTO
                .collect(Collectors.toList());

        logger.info("Recommended mentors for mentee with ID {}: {}", menteeId, recommendedMentors);

        return recommendedMentors;
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUserName(user.getUserName());
        userDTO.setUserMail(user.getUserMail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setExpertise(user.getExpertise());
        userDTO.setLocation(user.getLocation());
        userDTO.setBio(user.getBio());
        userDTO.setProfileImage(user.getProfileImage());
        userDTO.setIndustry(user.getIndustry());
        userDTO.setMentoringRequiredFor(user.getMentoringRequiredFor());
        userDTO.setChargePerHour(user.getChargePerHour());

        return userDTO;
    }


    @Override
    public List<UserDTO> getUsersWithAcceptedConnectionAndAverageRating(Long userId) {
        List<UserDTO> usersWithAcceptedConnection = new ArrayList<>();

        List<ConnectionRequest> connections = connectionRequestRepo.findByMenteeIdOrMentorId(userId, userId);

        for (ConnectionRequest connection : connections) {
            if (connection.getStatus() == ConnectionRequestStatus.ACCEPTED) {
                Optional<User> connectedUserOptional = getConnectedUser(connection, userId);
                if (connectedUserOptional.isPresent()) {
                    User connectedUser = connectedUserOptional.get();
                    UserDTO userDTO = createUserDTOWithAverageRating(connectedUser);
                    usersWithAcceptedConnection.add(userDTO);
                }
            }
        }

        return usersWithAcceptedConnection;
    }

    public UserDTO createUserDTOWithAverageRating(User connectedUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(connectedUser.getId());
        userDTO.setUserName(connectedUser.getUserName());
        userDTO.setUserMail(connectedUser.getUserMail());
        userDTO.setBio(connectedUser.getBio());
        userDTO.setProfileImage(connectedUser.getProfileImage());
        userDTO.setPhoneNumber(connectedUser.getPhoneNumber());
        userDTO.setExpertise(connectedUser.getExpertise());
        userDTO.setIndustry(connectedUser.getIndustry());
        userDTO.setLocation(connectedUser.getLocation());
        userDTO.setMentoringRequiredFor(connectedUser.getMentoringRequiredFor());

        Double averageRating = null;
        if (connectedUser.getRoles() != null) {
            if (connectedUser.getRoles().contains(UserRole.MENTOR)) {
                averageRating = feedbackRepository.calculateAverageRatingForMentorId(connectedUser.getId());
            } else if (connectedUser.getRoles().contains(UserRole.MENTEE)) {
                averageRating = feedbackRepository.calculateAverageRatingForMenteeId(connectedUser.getId());
            }
        }

        userDTO.setAverageRating(averageRating);

        return userDTO;
    }


    public Optional<User> getConnectedUser(ConnectionRequest connection, Long userId) {
        if (connection.getMentee() != null && connection.getMentee().getId().equals(userId)) {
            return Optional.of(connection.getMentor());
        } else if (connection.getMentor() != null && connection.getMentor().getId().equals(userId)) {
            return Optional.of(connection.getMentee());
        }
        return Optional.empty();
    }


    @Override
    public ConnectionRequestStatus getConnectionStatus(Long menteeId, Long mentorId) {
        logger.info("Getting connection status between mentee ID: {} and mentor ID: {}", menteeId, mentorId);

        ConnectionRequest connectionRequest = connectionRequestRepo.findByMenteeIdAndMentorId(menteeId, mentorId);
        if (connectionRequest == null) {

            logger.info("Connection between mentee ID: {} and mentor ID: {} does not exist. Returning default status.", menteeId, mentorId);
            return ConnectionRequestStatus.PENDING; // Return a default status here
        }

        logger.info("Retrieved connection status: {} between mentee ID: {} and mentor ID: {}", connectionRequest.getStatus(), menteeId, mentorId);

        return connectionRequest.getStatus();
    }


    @Override
    public List<Map<String, Object>> getConnectionDetailsByUserId(Long userId) {
        logger.info("Getting connection details for user with ID: {}", userId);

        List<Object[]> results = connectionRequestRepo.findConnectionDetailsByUserId(userId);
        List<Map<String, Object>> connectionDetails = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> connection = new HashMap<>();
            connection.put("menteeName", row[0]);
            connection.put("mentorName", row[1]);
            connection.put("menteeId", row[2]);
            connection.put("mentorId", row[3]);
            connection.put("message", row[4]);
            connectionDetails.add(connection);
        }

        logger.info("Retrieved connection details for user with ID: {}", userId);

        return connectionDetails;
    }


}
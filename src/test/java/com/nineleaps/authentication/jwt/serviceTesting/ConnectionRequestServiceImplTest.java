package com.nineleaps.authentication.jwt.serviceTesting;

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
import com.nineleaps.authentication.jwt.service.implementation.ConnectionRequestServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.EmailServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionRequestServiceImplTest {

    @Mock
    private ConnectionRequestRepo connectionRequestRepo;
    @Mock
    private UserServiceImpl userServiceImpl;
    @Mock
    private EmailServiceImpl emailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FeedbackRepository feedbackRepository;
    @InjectMocks
    private ConnectionRequestServiceImpl connectionRequestServiceImpl;
    private Logger logger = LoggerFactory.getLogger(ConnectionRequestServiceImpl.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        connectionRequestServiceImpl = new ConnectionRequestServiceImpl(
                userRepository,
                connectionRequestRepo,
                userServiceImpl,
                emailService,
                feedbackRepository
        );
    }

    @Test
    void testGetConnectionDetailsByUserId() {
        // Arrange
        Long userId = 1L;
        List<Object[]> results = new ArrayList<>();
        Object[] row1 = {"Mentee1", "Mentor1", 11L, 12L, "Message1"};
        Object[] row2 = {"Mentee2", "Mentor2", 21L, 22L, "Message2"};
        results.add(row1);
        results.add(row2);

        // Mocking the behavior of connectionRequestRepo
        when(connectionRequestRepo.findConnectionDetailsByUserId(userId)).thenReturn(results);

        // Act
        List<Map<String, Object>> connectionDetails = connectionRequestServiceImpl.getConnectionDetailsByUserId(userId);

        // Assert
        assertNotNull(connectionDetails);
        assertEquals(2, connectionDetails.size());

        Map<String, Object> connection1 = connectionDetails.get(0);
        assertEquals("Mentee1", connection1.get("menteeName"));
        assertEquals("Mentor1", connection1.get("mentorName"));
        assertEquals(11L, connection1.get("menteeId"));
        assertEquals(12L, connection1.get("mentorId"));
        assertEquals("Message1", connection1.get("message"));

        Map<String, Object> connection2 = connectionDetails.get(1);
        assertEquals("Mentee2", connection2.get("menteeName"));
        assertEquals("Mentor2", connection2.get("mentorName"));
        assertEquals(21L, connection2.get("menteeId"));
        assertEquals(22L, connection2.get("mentorId"));
        assertEquals("Message2", connection2.get("message"));
    }

    @Test
    void testGetConnectionStatusWhenConnectionExists() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setStatus(ConnectionRequestStatus.ACCEPTED);

        // Mock the behavior of connectionRequestRepo
        when(connectionRequestRepo.findByMenteeIdAndMentorId(menteeId, mentorId)).thenReturn(connectionRequest);

        // Act
        ConnectionRequestStatus status = connectionRequestServiceImpl.getConnectionStatus(menteeId, mentorId);

        // Assert
        assertEquals(ConnectionRequestStatus.ACCEPTED, status);
    }


//

    @Test
    void testGetUsersWithAcceptedConnectionAndAverageRatingWhenConnectionIsNull() {
        // Arrange
        Long userId = 1L;

        List<ConnectionRequest> connections = new ArrayList<>();

        // Mocking the connectionRequestRepo to return the empty list of connections
        when(connectionRequestRepo.findByMenteeIdOrMentorId(userId, userId)).thenReturn(connections);

        // Act
        List<UserDTO> result = connectionRequestServiceImpl.getUsersWithAcceptedConnectionAndAverageRating(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }




    @Test
    void testRecommendMentors_Success() throws ResourceNotFoundException {
        // Arrange
        Long connectionRequestId = 1L;
        List<Long> recommendedMentorIds = Arrays.asList(2L, 3L);

        User mentee = new User(1L);
        User mentor = new User(2L);

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setMentee(mentee);
        connectionRequest.setMentor(mentor);

        when(connectionRequestRepo.findById(connectionRequestId)).thenReturn(Optional.of(connectionRequest));

        // Act
        ConnectionRequestDto result = connectionRequestServiceImpl.recommendMentors(connectionRequestId, recommendedMentorIds);

        // Assert
        assertNotNull(result);
        assertEquals(recommendedMentorIds, result.getRecommendedMentors());
        verify(connectionRequestRepo, times(1)).findById(connectionRequestId);
        verify(connectionRequestRepo, times(1)).save(connectionRequest);
    }


    @Test
    void testRecommendMentors_ConnectionRequestNotFound() {
        // Arrange
        Long connectionRequestId = 1L;
        List<Long> recommendedMentorIds = Arrays.asList(2L, 3L);
        when(connectionRequestRepo.findById(connectionRequestId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            connectionRequestServiceImpl.recommendMentors(connectionRequestId, recommendedMentorIds);
        });
    }

    @Test
    void testRecommendMentors_EmptyRecommendationList() throws ResourceNotFoundException {
        // Arrange
        Long connectionRequestId = 1L;
        List<Long> recommendedMentorIds = Arrays.asList();

        User mentee = new User(1L);
        User mentor = new User(2L);

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setMentee(mentee);
        connectionRequest.setMentor(mentor);

        when(connectionRequestRepo.findById(connectionRequestId)).thenReturn(Optional.of(connectionRequest));

        // Act
        ConnectionRequestDto result = connectionRequestServiceImpl.recommendMentors(connectionRequestId, recommendedMentorIds);

        // Assert
        assertNotNull(result);
        assertEquals(recommendedMentorIds, result.getRecommendedMentors());

        verify(connectionRequestRepo, times(1)).findById(connectionRequestId);
        verify(connectionRequestRepo, times(1)).save(connectionRequest);
    }

    @Test
    void testConvertToDTO() {

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setId(1L);
        connectionRequest.setMessage("Hello, mentor!");
        connectionRequest.setStatus(ConnectionRequestStatus.PENDING);
        User mentee = new User();
        mentee.setId(2L);
        connectionRequest.setMentee(mentee);
        User mentor = new User();
        mentor.setId(2L);
        connectionRequest.setMentor(mentor);
        ConnectionRequestDto connectionRequestDto = connectionRequestServiceImpl.convertToDTO(connectionRequest);
        assertEquals(1L, connectionRequestDto.getId());
        assertEquals("Hello, mentor!", connectionRequestDto.getMessage());
        assertEquals(ConnectionRequestStatus.PENDING, connectionRequestDto.getStatus());
        assertEquals(2L, connectionRequestDto.getMenteeId());
    }

    @Test
    void testMapToConnectionRequestDto() {
        User mentee = new User(2L);
        User mentor = new User(3L);

        // Create a mock ConnectionRequest
        ConnectionRequest connectionRequest = mock(ConnectionRequest.class);
        when(connectionRequest.getId()).thenReturn(1L);
        when(connectionRequest.getMentor()).thenReturn(mentor);
        when(connectionRequest.getMentee()).thenReturn(mentee);
        when(connectionRequest.getMessage()).thenReturn("Test message");
        when(connectionRequest.getStatus()).thenReturn(ConnectionRequestStatus.PENDING);

        ConnectionRequestDto connectionRequestDto = connectionRequestServiceImpl.mapToConnectionRequestDto(connectionRequest);

        // Assert the results
        assertEquals(1L, connectionRequestDto.getId());
        assertEquals(3L, connectionRequestDto.getMentorId());
        assertEquals(2L, connectionRequestDto.getMenteeId());
        assertEquals("Test message", connectionRequestDto.getMessage());

    }

    @Test
    void testFindConnectionsByMenteeId() {
        Long menteeId = 1L;

        List<Object[]> mockConnections = new ArrayList<>();
        mockConnections.add(new Object[]{"Mentor 1", "Mentee 1", 1L, 2L});
        mockConnections.add(new Object[]{"Mentor 2", "Mentee 1", 3L, 1L});
        when(connectionRequestRepo.findConnectionsByMenteeId(menteeId)).thenReturn(mockConnections);

        List<Object[]> connections = connectionRequestServiceImpl.findConnectionsByMenteeId(menteeId);

        // Assertions
        assertEquals(2, connections.size());

        Object[] firstConnection = connections.get(0);
        assertEquals("Mentor 1", firstConnection[0]);
        assertEquals("Mentee 1", firstConnection[1]);
        assertEquals(1L, firstConnection[2]);
        assertEquals(2L, firstConnection[3]);

        Object[] secondConnection = connections.get(1);
        assertEquals("Mentor 2", secondConnection[0]);
        assertEquals("Mentee 1", secondConnection[1]);
        assertEquals(3L, secondConnection[2]);
        assertEquals(1L, secondConnection[3]);
    }


    @Test
    void testGetAllConnectionRequestsByUserIdWithPagination() {
        // Arrange
        Long userId = 1L;
        int page = 0;
        int size = 10;

        // Create a list of mock ConnectionRequest objects
        List<ConnectionRequest> mockConnectionRequests = new ArrayList<>();
        ConnectionRequest request1 = mock(ConnectionRequest.class);
        ConnectionRequest request2 = mock(ConnectionRequest.class);

        when(request1.getId()).thenReturn(1L);
        when(request1.getMentor()).thenReturn(new User(2L));
        when(request1.getMentee()).thenReturn(new User(3L));
        when(request1.getMessage()).thenReturn("Message1");

        when(request2.getId()).thenReturn(2L);
        when(request2.getMentor()).thenReturn(new User(4L));
        when(request2.getMentee()).thenReturn(new User(5L));
        when(request2.getMessage()).thenReturn("Message2");

        mockConnectionRequests.add(request1);
        mockConnectionRequests.add(request2);

        // Mock the userRepository to return a User
        User user = new User();
        user.setId(userId);
        user.setUserName("User1");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(UserRole.MENTOR);
        user.setRoles(userRoles);
        lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock the connectionRequestRepo to return a Page of ConnectionRequest objects
        Pageable pageable = PageRequest.of(page, size);
        Page<ConnectionRequest> connectionRequestsPage = new PageImpl<>(mockConnectionRequests);
        when(connectionRequestRepo.findAllByMenteeIdOrMentorId(userId, userId, pageable)).thenReturn(connectionRequestsPage);

        // Act
        List<ConnectionRequestDto> connectionRequests = connectionRequestServiceImpl.getAllConnectionRequestsByUserId(userId, page, size);

        // Assert
        assertEquals(mockConnectionRequests.size(), connectionRequests.size());

        // You can further assert the contents of connectionRequests based on the mockConnectionRequests.
        // For example:
        assertEquals(mockConnectionRequests.get(0).getId(), connectionRequests.get(0).getId());
        assertEquals(mockConnectionRequests.get(1).getId(), connectionRequests.get(1).getId());
        assertEquals(mockConnectionRequests.get(0).getMessage(), connectionRequests.get(0).getMessage());
        assertEquals(mockConnectionRequests.get(1).getMessage(), connectionRequests.get(1).getMessage());
        // Add more specific assertions as needed.
    }
    @Test
    void testSendConnectionRequest() {
        // Arrange
        Long mentorId = 1L;
        Long menteeId = 2L;
        String message = "Test message";

        User mentor = new User();
        mentor.setId(mentorId);

        User mentee = new User();
        mentee.setId(menteeId);

        ConnectionRequest existingRequest = new ConnectionRequest();
        existingRequest.setMentor(mentor);
        existingRequest.setMentee(mentee);
        existingRequest.setStatus(ConnectionRequestStatus.REJECTED);

        // Mocking userRepository findById
        when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));

        // Mocking connectionRequestRepo findByMentorAndMentee
        when(connectionRequestRepo.findByMentorAndMentee(mentor, mentee)).thenReturn(existingRequest);

        // Mock connectionRequestRepo save
        when(connectionRequestRepo.save(any(ConnectionRequest.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        ConnectionRequestDto result = connectionRequestServiceImpl.sendConnectionRequest(mentorId, menteeId, message);

        // Assert
        assertNotNull(result);
        assertEquals(mentorId, result.getMentorId());
        assertEquals(menteeId, result.getMenteeId());
        assertEquals(message, result.getMessage());
        assertEquals(ConnectionRequestStatus.PENDING, result.getStatus());
        assertNotNull(result.getRequestTime());
        assertNull(result.getAcceptanceTime());
        assertNull(result.getRejectionTime());

        // Verify interactions
        verify(userRepository, times(1)).findById(mentorId);
        verify(userRepository, times(1)).findById(menteeId);
        verify(connectionRequestRepo, times(1)).findByMentorAndMentee(mentor, mentee);
        verify(connectionRequestRepo, times(1)).save(any(ConnectionRequest.class));
    }


    @Test
    void testSendConnectionRequestDuplicateRequest() {
        // Prepare test data
        Long mentorId = 1L;
        Long menteeId = 2L;
        String message = "Test message";

        User mentor = new User();
        mentor.setId(mentorId);

        User mentee = new User();
        mentee.setId(menteeId);

        when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
        when(connectionRequestRepo.findByMentorAndMentee(mentor, mentee)).thenReturn(null);
        when(connectionRequestRepo.existsByMentorAndMentee(mentor, mentee)).thenReturn(true);

        assertThrows(DuplicateConnectionRequestException.class, () -> {
            connectionRequestServiceImpl.sendConnectionRequest(mentorId, menteeId, message);
        });

        verify(connectionRequestRepo, never()).save(any(ConnectionRequest.class));
    }

    @Test
    void testSendConnectionRequestNewConnectionRequest() {
        // Prepare test data
        Long mentorId = 1L;
        Long menteeId = 2L;
        String message = "Test message";

        User mentor = new User();
        mentor.setId(mentorId);

        User mentee = new User();
        mentee.setId(menteeId);

        when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
        when(connectionRequestRepo.findByMentorAndMentee(mentor, mentee)).thenReturn(null);
        when(connectionRequestRepo.existsByMentorAndMentee(mentor, mentee)).thenReturn(false);

        ConnectionRequestDto result = connectionRequestServiceImpl.sendConnectionRequest(mentorId, menteeId, message);

        verify(connectionRequestRepo, times(1)).save(any(ConnectionRequest.class));

        assertNotNull(result);
        assertEquals(mentorId, result.getMentorId());
        assertEquals(menteeId, result.getMenteeId());
        assertEquals(ConnectionRequestStatus.PENDING, result.getStatus());
    }


    @Test
    void testAcceptConnectionRequest_ConnectionRequestNotFound() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        when(connectionRequestRepo.findById(connectionRequestId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            connectionRequestServiceImpl.acceptConnectionRequest(connectionRequestId, mentorId, menteeId);
        });
    }

    @Test
    void testAcceptConnectionRequest_MenteeIdMismatch() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        User mentee = new User(4L);
        User mentor = new User(mentorId);
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setId(connectionRequestId);
        connectionRequest.setMentee(mentee);
        connectionRequest.setMentor(mentor);

        when(connectionRequestRepo.findById(connectionRequestId)).thenReturn(Optional.of(connectionRequest));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            connectionRequestServiceImpl.acceptConnectionRequest(connectionRequestId, mentorId, menteeId);
        });
    }

    @Test
    void testAcceptConnectionRequest_Success() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        User menteeUser = new User();
        menteeUser.setId(menteeId);
        menteeUser.setUserMail("mentee@example.com");

        User mentorUser = new User();
        mentorUser.setId(mentorId);

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setId(connectionRequestId);
        connectionRequest.setStatus(ConnectionRequestStatus.PENDING);
        connectionRequest.setMentee(menteeUser);
        connectionRequest.setMentor(mentorUser);

        when(connectionRequestRepo.findById(connectionRequestId)).thenReturn(Optional.of(connectionRequest));
        when(userServiceImpl.getMenteeEmailById(menteeId)).thenReturn("mentee@example.com");
        when(connectionRequestRepo.save(any(ConnectionRequest.class))).thenReturn(connectionRequest);

        // Act
        ConnectionRequestDto connectionRequestDto = connectionRequestServiceImpl.acceptConnectionRequest(connectionRequestId, mentorId, menteeId);

        // Assert
        assertNotNull(connectionRequestDto);
        assertEquals(ConnectionRequestStatus.ACCEPTED, connectionRequestDto.getStatus());
        assertNotNull(connectionRequestDto.getAcceptanceTime());
        assertTrue(connectionRequestDto.getAcceptanceTime().isBefore(LocalDateTime.now()));
    }

    @Test
    void testRejectConnectionRequest_Success() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        User menteeUser = new User();
        menteeUser.setId(menteeId);

        User mentorUser = new User();
        mentorUser.setId(mentorId);

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setId(connectionRequestId);
        connectionRequest.setStatus(ConnectionRequestStatus.PENDING);
        connectionRequest.setMentee(menteeUser);
        connectionRequest.setMentor(mentorUser);

        when(connectionRequestRepo.findById(connectionRequestId)).thenReturn(Optional.of(connectionRequest));
        when(connectionRequestRepo.save(any(ConnectionRequest.class))).thenReturn(connectionRequest);

        // Act
        ConnectionRequestDto connectionRequestDto = connectionRequestServiceImpl.rejectConnectionRequest(connectionRequestId, mentorId, menteeId);

        // Assert
        assertNotNull(connectionRequestDto);
        assertEquals(ConnectionRequestStatus.REJECTED, connectionRequestDto.getStatus());
        assertNotNull(connectionRequestDto.getRejectionTime());
        assertTrue(connectionRequestDto.getRejectionTime().isBefore(LocalDateTime.now()));
    }

    @Test
    void testRejectConnectionRequest_ConnectionRequestNotFound() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        // Mocking the repository to return an empty Optional, indicating that the connection request is not found
        when(connectionRequestRepo.findById(connectionRequestId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            connectionRequestServiceImpl.rejectConnectionRequest(connectionRequestId, mentorId, menteeId);
        });
    }

    @Test
    void testRejectConnectionRequest_MentorIdMismatch() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        User menteeUser = new User();
        menteeUser.setId(menteeId);

        User mentorUser = new User();
        mentorUser.setId(999L);

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setId(connectionRequestId);
        connectionRequest.setStatus(ConnectionRequestStatus.PENDING);
        connectionRequest.setMentee(menteeUser);
        connectionRequest.setMentor(mentorUser);

        // Mocking the repository to return the connection request
        when(connectionRequestRepo.findById(connectionRequestId)).thenReturn(Optional.of(connectionRequest));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            connectionRequestServiceImpl.rejectConnectionRequest(connectionRequestId, mentorId, menteeId);
        });
    }

    @Test
    void testSendConnectionRequest_ExistingRequestNotRejected() {
        // Arrange
        Long mentorId = 1L;
        Long menteeId = 2L;
        String message = "Test message";

        User mentor = new User();
        mentor.setId(mentorId);

        User mentee = new User();
        mentee.setId(menteeId);

        ConnectionRequest existingRequest = new ConnectionRequest();
        existingRequest.setMentor(mentor);
        existingRequest.setMentee(mentee);
        existingRequest.setStatus(ConnectionRequestStatus.PENDING);

        // Mock userRepository findById
        when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));

        // Mock connectionRequestRepo findByMentorAndMentee
        when(connectionRequestRepo.findByMentorAndMentee(mentor, mentee)).thenReturn(existingRequest);

        // Mock connectionRequestRepo save
        when(connectionRequestRepo.save(any(ConnectionRequest.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        ConnectionRequestDto result = connectionRequestServiceImpl.sendConnectionRequest(mentorId, menteeId, message);

        // Assert
        assertNotNull(result);
        assertEquals(mentorId, result.getMentorId());
        assertEquals(menteeId, result.getMenteeId());
        assertEquals(message, result.getMessage());
        assertEquals(ConnectionRequestStatus.PENDING, result.getStatus());
        assertNotNull(result.getRequestTime());
        assertNull(result.getAcceptanceTime());
        assertNull(result.getRejectionTime());

        // Verify interactions
        verify(userRepository, times(1)).findById(mentorId);
        verify(userRepository, times(1)).findById(menteeId);
        verify(connectionRequestRepo, times(1)).findByMentorAndMentee(mentor, mentee);
        verify(connectionRequestRepo, times(1)).save(any(ConnectionRequest.class));
    }

//   Get Recommended mentors by mentee Id
@Test
void testGetRecommendedMentorsByMenteeId() {
    // Arrange
    Long menteeId = 25L;
    // Create a mock ConnectionRequest object
    ConnectionRequest connectionRequest = new ConnectionRequest();
    User recommendedMentor1 = new User(2L);
    User recommendedMentor2 = new User(3L);

    connectionRequest.setMentor(recommendedMentor1);
    connectionRequest.setMentor(recommendedMentor2);
    connectionRequest.setRecommendedMentors(Arrays.asList(2L, 3L));

    List<ConnectionRequest> connectionRequests = Arrays.asList(connectionRequest);

    when(connectionRequestRepo.findByMenteeId(menteeId)).thenReturn(connectionRequests);

    // Act
    List<UserDTO> recommendedMentors = connectionRequestServiceImpl.getRecommendedMentorsByMenteeId(menteeId);

    // Assert
    assertEquals(0, recommendedMentors.size());

    verify(connectionRequestRepo, times(1)).findByMenteeId(menteeId);

}


    @Test
    void testConvertToUserDTO() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUserName("hadi");
        user.setUserMail("hadi@gmail.com");
        user.setPhoneNumber("123-456-7890");
        user.setExpertise("Software Development");
        user.setLocation("India");
        user.setBio("Experienced software developer");
        user.setIndustry("IT");
        user.setMentoringRequiredFor("Java Programming");
        user.setChargePerHour(50.0);

        // Act
        UserDTO userDTO = connectionRequestServiceImpl.convertToUserDTO(user);

        // Assert
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getUserName(), userDTO.getUserName());
        assertEquals(user.getUserMail(), userDTO.getUserMail());
        assertEquals(user.getPhoneNumber(), userDTO.getPhoneNumber());
        assertEquals(user.getExpertise(), userDTO.getExpertise());
        assertEquals(user.getLocation(), userDTO.getLocation());
        assertEquals(user.getBio(), userDTO.getBio());
        assertEquals(user.getProfileImage(), userDTO.getProfileImage());
        assertEquals(user.getIndustry(), userDTO.getIndustry());
        assertEquals(user.getMentoringRequiredFor(), userDTO.getMentoringRequiredFor());
        assertEquals(user.getChargePerHour(), userDTO.getChargePerHour(), 0.01);
    }

    @Test
    void testGetUsersWithAcceptedConnectionAndAverageRating() {
        User user = new User();
        user.setId(1L);
        user.setUserName("TestUser");

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setStatus(ConnectionRequestStatus.ACCEPTED);
        connectionRequest.setMentor(user);
        connectionRequest.setMentee(user);

        List<ConnectionRequest> connectionRequests = new ArrayList<>();
        connectionRequests.add(connectionRequest);

        // Mocking the behavior of the connection request repository
        when(connectionRequestRepo.findByMenteeIdOrMentorId(user.getId(), user.getId()))
                .thenReturn(connectionRequests);


        List<UserDTO> result = connectionRequestServiceImpl.getUsersWithAcceptedConnectionAndAverageRating(user.getId());


        verify(connectionRequestRepo, times(1)).findByMenteeIdOrMentorId(user.getId(), user.getId());
    }
    @Test
    void testGetUsersWithNoAcceptedConnections() {
        User user = new User();
        user.setId(1L);

        List<ConnectionRequest> connectionRequests = new ArrayList<>();

        // Mocking the behavior of the connection request repository
        when(connectionRequestRepo.findByMenteeIdOrMentorId(user.getId(), user.getId()))
                .thenReturn(connectionRequests);

        List<UserDTO> result = connectionRequestServiceImpl.getUsersWithAcceptedConnectionAndAverageRating(user.getId());

        // Assert that the result list is empty
        assertTrue(result.isEmpty());
    }
    @Test
    void testGetUsersWithMixedConnections() {
        User user = new User();
        user.setId(1L);

        ConnectionRequest acceptedConnectionRequest = new ConnectionRequest();
        acceptedConnectionRequest.setStatus(ConnectionRequestStatus.ACCEPTED);
        acceptedConnectionRequest.setMentor(user);
        acceptedConnectionRequest.setMentee(user);

        ConnectionRequest nonAcceptedConnectionRequest = new ConnectionRequest();
        nonAcceptedConnectionRequest.setStatus(ConnectionRequestStatus.PENDING);
        nonAcceptedConnectionRequest.setMentor(user);
        nonAcceptedConnectionRequest.setMentee(user);

        // Create a list of connection requests to be returned by the repository
        List<ConnectionRequest> connectionRequests = new ArrayList<>();
        connectionRequests.add(acceptedConnectionRequest);
        connectionRequests.add(nonAcceptedConnectionRequest);

        // Mock the behavior of the connection request repository
        when(connectionRequestRepo.findByMenteeIdOrMentorId(user.getId(), user.getId()))
                .thenReturn(connectionRequests);

        List<UserDTO> result = connectionRequestServiceImpl.getUsersWithAcceptedConnectionAndAverageRating(user.getId());

        assertEquals(1, result.size());
    }
    @Test
    void testCreateUserDTOWithAverageRatingForMentor() {
        User connectedUser = new User();
        connectedUser.setId(1L);
        connectedUser.setRoles(Collections.singleton(UserRole.MENTOR));

        // Mock the behavior of feedbackRepository for a mentor
        when(feedbackRepository.calculateAverageRatingForMentorId(connectedUser.getId())).thenReturn(4.5);

        UserDTO userDTO = connectionRequestServiceImpl.createUserDTOWithAverageRating(connectedUser);

        verify(feedbackRepository).calculateAverageRatingForMentorId(connectedUser.getId());

        // Assert that the userDTO has the correct averageRating value
        assertEquals(4.5, userDTO.getAverageRating(), 0.01); // Adjust delta as needed
    }

    @Test
    void testCreateUserDTOWithAverageRatingForMentee() {
        User connectedUser = new User();
        connectedUser.setId(1L);
        connectedUser.setRoles(Collections.singleton(UserRole.MENTEE));

        // Mock the behavior of feedbackRepository for a mentee
        when(feedbackRepository.calculateAverageRatingForMenteeId(connectedUser.getId())).thenReturn(3.8);

        UserDTO userDTO = connectionRequestServiceImpl.createUserDTOWithAverageRating(connectedUser);

        verify(feedbackRepository).calculateAverageRatingForMenteeId(connectedUser.getId());

        assertEquals(3.8, userDTO.getAverageRating(), 0.01); // Adjust delta as needed
    }

    @Test
    void testCreateUserDTOWithAverageRatingForNoRoles() {
        // Create a connectedUser with no
        User connectedUser = new User();
        connectedUser.setId(1L);
        connectedUser.setRoles(Collections.emptySet()); // No roles

        UserDTO userDTO = connectionRequestServiceImpl.createUserDTOWithAverageRating(connectedUser);

        // Verify that no method was called on feedbackRepository since there are no roles
        verifyNoInteractions(feedbackRepository);

        assertNull(userDTO.getAverageRating());
    }

    @Test
    void testGetConnectedUserNoMatch() {
        ConnectionRequest connection = new ConnectionRequest();
        connection.setMentee(new User());
        connection.getMentee().setId(2L);
        connection.setMentor(new User());
        connection.getMentor().setId(3L);

        Optional<User> connectedUser = connectionRequestServiceImpl.getConnectedUser(connection, 1L);

        assertFalse(connectedUser.isPresent());
    }

    @Test
    void testGetConnectedUserWhenMentorMatchesUserId() {
        ConnectionRequest connection = new ConnectionRequest();
        connection.setMentee(new User());
        connection.getMentee().setId(2L);
        connection.setMentor(new User());
        connection.getMentor().setId(1L);

        Optional<User> connectedUser = connectionRequestServiceImpl.getConnectedUser(connection, 1L);

        assertTrue(connectedUser.isPresent());
        assertEquals(2L, connectedUser.get().getId());
    }


    @ParameterizedTest
    @CsvSource({
            "1, 2",
            "2, 1",
            "3, 4"
    })
    void testGetConnectedUser(Long menteeId, Long mentorId) {
        ConnectionRequest connection = new ConnectionRequest();
        connection.setMentee(new User());
        connection.getMentee().setId(menteeId);
        connection.setMentor(new User());
        connection.getMentor().setId(mentorId);

        Optional<User> connectedUser = connectionRequestServiceImpl.getConnectedUser(connection, menteeId);

        assertTrue(connectedUser.isPresent());
        assertEquals(mentorId, connectedUser.get().getId());
    }


}
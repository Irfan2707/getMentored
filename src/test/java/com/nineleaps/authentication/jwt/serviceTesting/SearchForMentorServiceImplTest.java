package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.repository.ConnectionRequestRepo;
import com.nineleaps.authentication.jwt.repository.FeedbackRepository;
import com.nineleaps.authentication.jwt.repository.SearchRepository;
import com.nineleaps.authentication.jwt.service.implementation.SearchForMentorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class SearchForMentorServiceImplTest {

    @InjectMocks
    private SearchForMentorServiceImpl searchForMentorService;

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private ConnectionRequestRepo connectionRequestRepository;
    @Mock
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(SearchForMentorServiceImpl.class);
    private String expertise;
    private List<User> users;
    private String name;
    private String industry;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
        name = "irfan";
        expertise="java";
        industry="Technology";
        users = new ArrayList<>();
        User user1 = new User();
        user1.setUserName("aqib");
        User user2 = new User();
        user2.setUserName("irfan");
        users.add(user1);
        users.add(user2);
//        users.add(user3);

    }

    @Test
     void testGetUserByName() {

        // Mock the behavior of searchRepository
        when(searchRepository.findByuserNameContainingIgnoreCase(name)).thenReturn(users);

        // Act
        List<User> result = searchForMentorService.getUserByName(name);
        logger.info("Users retrieved! {}",result);

        // Assert
        verify(searchRepository, times(1)).findByuserNameContainingIgnoreCase(name);

        assertAll(() -> {
            assertEquals(2, result.size());

            assertTrue(result.contains(users.get(0)));
            assertTrue(result.contains(users.get(1)));
        });
    }



    @Test
     void testGetUserByExpertise() {

        // Mock the behavior of searchRepository
        when(searchRepository.findByExpertiseContaining(expertise)).thenReturn(users);

        // Act
        List<User> result = searchForMentorService.getUserByExpertise(expertise);

        // Assert
        verify(searchRepository, times(1)).findByExpertiseContaining(expertise);

        assertAll(() -> {
            assertEquals(2, result.size());

            assertTrue(result.contains(users.get(0)));
            assertTrue(result.contains(users.get(1)));
        });
    }

    @Test
     void testGetUserByIndustry() {
        when(searchRepository.findByIndustryContainingIgnoreCase(industry)).thenReturn(users);

        // Act
        List<User> result = searchForMentorService.getUserByIndustry(industry);

        // Assert
        verify(searchRepository, times(1)).findByIndustryContainingIgnoreCase(industry);

        assertAll(() -> {
            // Check the size of the result list
            assertEquals(2, result.size());

            assertTrue(result.contains(users.get(0)));
            assertTrue(result.contains(users.get(1)));
        });
    }
//Getting users by role
@Test
void testGetUsersByRole() {
   // Create a mock User object
   User user = new User();
   user.setId(1L);

   when(searchRepository.findByRoles(UserRole.MENTOR)).thenReturn(List.of(user));

   MentorDTO mentorDTO = new MentorDTO();
   mentorDTO.setId(1L);

   when(modelMapper.map(user, MentorDTO.class)).thenReturn(mentorDTO);

   List<MentorDTO> resultMentorDTOList = searchForMentorService.getUsersByRole(UserRole.MENTOR);

   assertEquals(1, resultMentorDTOList.size());
   MentorDTO resultMentorDTO = resultMentorDTOList.get(0);
   assertEquals(1L, resultMentorDTO.getId());
}

    @Test
    void testGetUsersByRoleEmptyList() {
       when(searchRepository.findByRoles(UserRole.MENTOR)).thenReturn(new ArrayList<>());

       List<MentorDTO> resultMentorDTOList = searchForMentorService.getUsersByRole(UserRole.MENTOR);

       assertEquals(0, resultMentorDTOList.size());
    }


//    Get Users by name ,expertise or industry
    @Test
    void testGetUserByNameOrExpertiseOrIndustry() {
       // Arrange
       String nameToSearch = "User1";
       String expertiseToSearch = "Java";
       String industryToSearch = "IT";

       List<User> expectedUsers = new ArrayList<>();


       when(searchRepository.findByuserNameOrExpertiseOrIndustry(nameToSearch, expertiseToSearch, industryToSearch))
               .thenReturn(expectedUsers);

       // Act
       List<User> actualUsers = searchForMentorService.getUserByNameOrExpertiseOrIndustry(
               nameToSearch, expertiseToSearch, industryToSearch);

       // Assert
       verify(searchRepository, times(1))
               .findByuserNameOrExpertiseOrIndustry(nameToSearch, expertiseToSearch, industryToSearch);

       assertEquals(expectedUsers.size(), actualUsers.size());
       for (int i = 0; i < expectedUsers.size(); i++) {
          User expectedUser = expectedUsers.get(i);
          User actualUser = actualUsers.get(i);
          assertEquals(expectedUser.getId(), actualUser.getId());
          assertEquals(expectedUser.getUserName(), actualUser.getUserName());
          assertEquals(expectedUser.getUserMail(), actualUser.getUserMail());
       }
    }
//    Getting users by keyword
@Test
void testGetUsersByKeyword() {
   // Arrange
   String keywordToSearch = "Java";


   List<User> expectedUsers = new ArrayList<>();


   when(searchRepository.findByKeyword(keywordToSearch)).thenReturn(expectedUsers);

   // Act
   List<User> actualUsers = searchForMentorService.getUsersByKeyword(keywordToSearch);

   // Assert
   verify(searchRepository, times(1)).findByKeyword(keywordToSearch);

   assertEquals(expectedUsers.size(), actualUsers.size());
   for (int i = 0; i < expectedUsers.size(); i++) {
      User expectedUser = expectedUsers.get(i);
      User actualUser = actualUsers.get(i);
      assertEquals(expectedUser.getId(), actualUser.getId());
      assertEquals(expectedUser.getUserName(), actualUser.getUserName());
      assertEquals(expectedUser.getUserMail(), actualUser.getUserMail());
   }
}

//Get by mentorDto
    @Test
     void testGetByMentorDto() {
        // Arrange
        String keyword = "Java";
        Long mentorId = 1L;
        Long menteeId = 1L;

        User mockUser = new User();
        mockUser.setId(mentorId);
        mockUser.setUserName("irfan");

        // Create a list of mock Users
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(mockUser);

        when(searchRepository.findByMentorDto(keyword)).thenReturn(mockUsers);

        // Mock the behavior of feedbackRepository to return average ratings
        when(feedbackRepository.calculateAverageRatingForMentorId(mentorId)).thenReturn(4.5);
        when(feedbackRepository.calculateAverageRatingForMenteeId(menteeId)).thenReturn(4.2);


       MentorDTO mockMentorDTO = new MentorDTO();
       mockMentorDTO.setId(1L);
       mockMentorDTO.setUserName("Mentor1");
       mockMentorDTO.setExpertise("Java, Spring");
       mockMentorDTO.setAverageMentorRating(4.5);
       mockMentorDTO.setAverageMenteeRating(4.2);

       Mockito.when(modelMapper.map(mockUser, MentorDTO.class)).thenReturn(mockMentorDTO);

       List<MentorDTO> mentorDTOs = searchForMentorService.getByMentorDto(keyword);

       // Verify the results
       assertEquals(1, mentorDTOs.size());
       MentorDTO resultMentorDTO = mentorDTOs.get(0);
       assertEquals(1L, resultMentorDTO.getId());
       assertEquals("Mentor1", resultMentorDTO.getUserName());
       assertEquals("Java, Spring", resultMentorDTO.getExpertise());
       assertEquals(4.5, resultMentorDTO.getAverageMentorRating());
       assertEquals(4.2, resultMentorDTO.getAverageMenteeRating());
    }

//Get sorted mentors
@Test
void testGetSortedMentorsNoMentors() {
   when(searchRepository.findByRoles(UserRole.MENTOR)).thenReturn(new ArrayList<>());

   List<MentorDTO> resultMentorDTOList = searchForMentorService.getSortedMentors("Java", "Tech", "Location", 2L);

   // Verify the result is an empty list
   assertEquals(0, resultMentorDTOList.size());
}

    @Test
    void testGetSortedMentors() {
       User user1 = new User();
       user1.setId(1L);
       User user2 = new User();
       user2.setId(2L);

       MentorDTO mentorDTO1 = new MentorDTO();
       mentorDTO1.setId(1L);
       mentorDTO1.setExpertise("Java, Spring");
       MentorDTO mentorDTO2 = new MentorDTO();
       mentorDTO2.setId(2L);
       mentorDTO2.setExpertise("Python");

       when(searchRepository.findByRoles(UserRole.MENTOR)).thenReturn(List.of(user1, user2));

       when(modelMapper.map(user1, MentorDTO.class)).thenReturn(mentorDTO1);
       when(modelMapper.map(user2, MentorDTO.class)).thenReturn(mentorDTO2);

       when(connectionRequestRepository.existsByMentorAndMenteeIdAndStatusIn(user1, 2L, new ConnectionRequestStatus[]{})).thenReturn(false);
       when(connectionRequestRepository.existsByMentorAndMenteeIdAndStatusIn(user2, 2L, new ConnectionRequestStatus[]{})).thenReturn(false);

       List<MentorDTO> resultMentorDTOList = searchForMentorService.getSortedMentors("Java", "Tech", "Location", 2L);

       // Verify the results
       assertEquals(2, resultMentorDTOList.size());
       assertEquals(1L, resultMentorDTOList.get(0).getId());
       assertEquals(2L, resultMentorDTOList.get(1).getId());
    }
    @Test
     void testFilterMentorsByConnectionStatus() {
        // Arrange
        MentorDTO mentor1 = new MentorDTO();
        mentor1.setId(1L);
        MentorDTO mentor2 = new MentorDTO();
        mentor2.setId(2L);
        List<MentorDTO> mentors = Arrays.asList(mentor1, mentor2);
        Long menteeId = 3L;

        // Mock connection status: mentor1 connected, mentor2 not connected
        when(searchForMentorService.mentorIsNotConnected(mentor1, menteeId)).thenReturn(false);
        when(searchForMentorService.mentorIsNotConnected(mentor2, menteeId)).thenReturn(true);

        // Act
        List<MentorDTO> filteredMentors = searchForMentorService.filterMentorsByConnectionStatus(mentors, menteeId);

        // Assert
        assertEquals(2, filteredMentors.size());
        assertEquals(mentor2.getId(), filteredMentors.get(1).getId());

    }
//  Testing sort method



     @Test
     void testSortMentors_ExpertiseComparison() {
         // Arrange
         MentorDTO mentor1 = new MentorDTO();
         mentor1.setId(1L);
         mentor1.setExpertise("Java, Spring");

         MentorDTO mentor2 = new MentorDTO();
         mentor2.setId(2L);
         mentor2.setExpertise("Python, Django");

         List<MentorDTO> mentors = new ArrayList<>();
         mentors.add(mentor1);
         mentors.add(mentor2);

         // Mock the expertise parameter as "Java"
         String expertise = "Java";

         // Act
         List<MentorDTO> sortedMentors = searchForMentorService.sortMentors(mentors, expertise, null, null);

         // Assert
         assertEquals(2, sortedMentors.size());
         assertEquals(1L, sortedMentors.get(0).getId());
         assertEquals(2L, sortedMentors.get(1).getId());
     }

     @Test
     void testSortMentors_IndustryComparison() {
         // Arrange
         MentorDTO mentor1 = new MentorDTO();
         mentor1.setId(1L);
         mentor1.setIndustry("Tech");

         MentorDTO mentor2 = new MentorDTO();
         mentor2.setId(2L);
         mentor2.setIndustry("Finance");

         List<MentorDTO> mentors = new ArrayList<>();
         mentors.add(mentor1);
         mentors.add(mentor2);

         String industry = "Finance";

         // Act
         List<MentorDTO> sortedMentors = searchForMentorService.sortMentors(mentors, null, industry, null);

         // Assert
         assertEquals(2, sortedMentors.size());
         assertEquals(2L, sortedMentors.get(0).getId());
         assertEquals(1L, sortedMentors.get(1).getId());
     }


     @Test
     void testSortMentors_LocationComparison() {
         // Arrange
         MentorDTO mentor1 = new MentorDTO();
         mentor1.setId(1L);
         mentor1.setLocation("New York");

         MentorDTO mentor2 = new MentorDTO();
         mentor2.setId(2L);
         mentor2.setLocation("San Francisco");

         List<MentorDTO> mentors = new ArrayList<>();
         mentors.add(mentor1);
         mentors.add(mentor2);

         String location = "San Francisco";

         // Act
         List<MentorDTO> sortedMentors = searchForMentorService.sortMentors(mentors, null, null, location);

         // Assert
         assertEquals(2, sortedMentors.size());

     }

     @Test
     void testSortMentors_ExpertiseComparison_NoEffect() {
         // Arrange
         MentorDTO mentor1 = new MentorDTO();
         mentor1.setId(1L);
         mentor1.setExpertise("Java");

         MentorDTO mentor2 = new MentorDTO();
         mentor2.setId(2L);
         mentor2.setExpertise("Java");

         List<MentorDTO> mentors = new ArrayList<>();
         mentors.add(mentor1);
         mentors.add(mentor2);

         // Mock the expertise parameter as "Java"
         String expertise = "Java";

         // Act
         List<MentorDTO> sortedMentors = searchForMentorService.sortMentors(mentors, expertise, null, null);

         // Assert
         assertEquals(2, sortedMentors.size());
         // Verify that mentor1 and mentor2 have the same expertise and should retain their original order
         assertEquals(1L, sortedMentors.get(0).getId());
         assertEquals(2L, sortedMentors.get(1).getId());
     }

     @Test
     void testSortMentors_UserNameComparison_NullHandling() {
         // Arrange
         MentorDTO mentor1 = new MentorDTO();
         mentor1.setId(1L);
         mentor1.setLocation("New York");
         mentor1.setUserName("Alice");

         MentorDTO mentor2 = new MentorDTO();
         mentor2.setId(2L);
         mentor2.setLocation("San Francisco");

         MentorDTO mentor3 = new MentorDTO();
         mentor3.setId(3L);
         mentor3.setLocation("Los Angeles");
         mentor3.setUserName("Bob");

         MentorDTO mentor4 = new MentorDTO();
         mentor4.setId(4L);
         mentor4.setLocation("Chicago");
         mentor4.setUserName(null);

         List<MentorDTO> mentors = new ArrayList<>();
         mentors.add(mentor1);
         mentors.add(mentor2);
         mentors.add(mentor3);
         mentors.add(mentor4);

         // Act
         List<MentorDTO> sortedMentors = searchForMentorService.sortMentors(mentors, null, null, null);

         // Assert
         assertEquals(4, sortedMentors.size());
         assertEquals(mentor4.getId(), sortedMentors.get(3).getId());
         assertEquals(mentor1.getId(), sortedMentors.get(0).getId());
     }


    @Test
     void testCalculateAverageRatings() {
        // Arrange
        MentorDTO mentor1 = new MentorDTO();
        mentor1.setId(1L);
        MentorDTO mentor2 = new MentorDTO();
        mentor2.setId(2L);
        List<MentorDTO> mentors = Arrays.asList(mentor1, mentor2);

        // Mock feedbackRepository to return average ratings
        when(feedbackRepository.calculateAverageRatingForMentorId(1L)).thenReturn(4.5);
        when(feedbackRepository.calculateAverageRatingForMentorId(2L)).thenReturn(3.8);

        // Act
        List<MentorDTO> mentorsWithRatings = searchForMentorService.calculateAverageRatings(mentors);

        assertEquals(4.5, mentorsWithRatings.get(0).getAverageMentorRating());
        assertEquals(3.8, mentorsWithRatings.get(1).getAverageMentorRating());
    }

    @Test
     void testCompareExpertise() {
        // Arrange
        String expertise1 = "Java, Python";
        String expertise2 = "Python, JavaScript";
        String expertise3=   "mysql";
        String targetExpertise = "Python";

        // Act and Assert
        assertEquals(0, searchForMentorService.compareExpertise(expertise1, expertise2, targetExpertise));
        assertEquals(-1, searchForMentorService.compareExpertise(expertise2, expertise3, targetExpertise));
        assertEquals(1, searchForMentorService.compareExpertise(expertise3, expertise1, targetExpertise));
    }

    @Test
     void testCompareValues() {
        // Arrange1
        String value1 = "New York";
        String value2 = "London";
        String value3=  "London";

        // Act and Assert
        assertEquals(2, searchForMentorService.compareValues(value1, value2));
        assertEquals(0, searchForMentorService.compareValues(value2, value3));
        assertEquals(-2, searchForMentorService.compareValues(value2, value1));
    }

    @Test
     void testMentorIsNotConnected() {
        // Arrange
        MentorDTO mentor = new MentorDTO();
        mentor.setId(1L);
        mentor.setUserName("JohnDoe");

        Long menteeId = 2L;

        // Mock the behavior of connectionRequestRepository
        when(connectionRequestRepository.existsByMentorAndMenteeIdAndStatusIn(any(), eq(menteeId), any())).thenReturn(false);

        // Act
        boolean result = searchForMentorService.mentorIsNotConnected(mentor, menteeId);

        // Assert
        assertTrue(result);
    }

    @Test
     void testMentorIsConnected() {
        // Arrange
        MentorDTO mentor = new MentorDTO();
        mentor.setId(1L);
        mentor.setUserName("JohnDoe");

        Long menteeId = 2L;

        // Mock the behavior of connectionRequestRepository
        when(connectionRequestRepository.existsByMentorAndMenteeIdAndStatusIn(any(), eq(menteeId), any())).thenReturn(true);

        // Act
        boolean result = searchForMentorService.mentorIsNotConnected(mentor, menteeId);

        // Assert
        assertFalse(result);
    }


}










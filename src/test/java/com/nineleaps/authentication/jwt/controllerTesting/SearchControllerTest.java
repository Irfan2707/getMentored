package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.SearchController;
import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.service.implementation.SearchForMentorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.nineleaps.authentication.jwt.controller.SearchController.USER_RETRIEVED_SUCCESSFULLY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchControllerTest {
    @Mock
    private SearchForMentorServiceImpl searchForMentorService;
    @InjectMocks
    private SearchController searchController;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsersByRoleSuccess() {
        // Arrange
        UserRole role = UserRole.MENTOR;
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setRoles(Collections.singleton(UserRole.MENTOR));

        List<MentorDTO> usersByRole = new ArrayList<>();
        usersByRole.add(mentorDTO);

        when(searchForMentorService.getUsersByRole(role)).thenReturn(usersByRole);

        // Act
        ResponseEntity<Object> responseEntity = searchController.getUsersByRole(role);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(USER_RETRIEVED_SUCCESSFULLY, responseBody.get("message"));
        assertEquals(usersByRole, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testGetUsersByRoleNoUsersFound() {
        // Arrange
        UserRole role = UserRole.MENTEE;

        when(searchForMentorService.getUsersByRole(role)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = searchController.getUsersByRole(role);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No users found for the specified role.", responseBody.get("error"));

    }


    @Test
    void testGetByNameOrExpertiseOrIndustrySuccess() {
        // Arrange
        String name = "Rasik";
        String expertise = "Programming";
        String industry = "Tech";

        List<User> users = new ArrayList<>();

        when(searchForMentorService.getUserByNameOrExpertiseOrIndustry(name, expertise, industry)).thenReturn(users);

        // Act
        ResponseEntity<Object> responseEntity = searchController.getByNameOrExpertiseOrIndustry(name, expertise, industry);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(USER_RETRIEVED_SUCCESSFULLY, responseBody.get("message"));
        assertEquals(users, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testGetByNameOrExpertiseOrIndustryNoUsersFound() {
        // Arrange
        String name = "Rasik";
        String expertise = "Programming";
        String industry = "Tech";

        when(searchForMentorService.getUserByNameOrExpertiseOrIndustry(name, expertise, industry)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = searchController.getByNameOrExpertiseOrIndustry(name, expertise, industry);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No users found for the specified search criteria.", responseBody.get("error"));

    }


    @Test
    void testSearchByKeywordSuccess() {
        // Arrange
        String keyword = "Programming";

        List<MentorDTO> mentors = new ArrayList<>();

        when(searchForMentorService.getByMentorDto(keyword)).thenReturn(mentors);

        // Act
        ResponseEntity<Object> responseEntity = searchController.searchByKeyword(keyword);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(USER_RETRIEVED_SUCCESSFULLY, responseBody.get("message"));
        assertEquals(mentors, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testSearchByKeywordNoUsersFound() {
        // Arrange
        String keyword = "Programming";

        when(searchForMentorService.getByMentorDto(keyword)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = searchController.searchByKeyword(keyword);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No users found for the specified keyword.", responseBody.get("error"));

    }


    @Test
    void testGetSortedMentorsSuccess() {
        // Arrange
        String expertise = "Programming";
        String industry = "IT";
        String location = "India";
        Long menteeId = 1L;

        List<MentorDTO> mentors = new ArrayList<>();

        when(searchForMentorService.getSortedMentors(expertise, industry, location, menteeId)).thenReturn(mentors);

        // Act
        ResponseEntity<Object> responseEntity = searchController.getSortedMentors(expertise, industry, location, menteeId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Mentors sorted successfully.", responseBody.get("message"));
        assertEquals(mentors, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testGetSortedMentorsNoMentorsFound() {
        // Arrange
        String expertise = "Programming";
        String industry = "IT";
        String location = "India";
        Long menteeId = 1L;

        when(searchForMentorService.getSortedMentors(expertise, industry, location, menteeId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = searchController.getSortedMentors(expertise, industry, location, menteeId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No mentors found for the specified criteria.", responseBody.get("error"));

    }


}
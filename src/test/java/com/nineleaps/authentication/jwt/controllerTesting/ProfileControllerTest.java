package com.nineleaps.authentication.jwt.controllerTesting;


import com.nineleaps.authentication.jwt.controller.ProfileController;
import com.nineleaps.authentication.jwt.dto.MenteeDTO;
import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.entity.Picture;
import com.nineleaps.authentication.jwt.service.implementation.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileControllerTest {
    @InjectMocks
    private ProfileController profileController;
    @Mock
    private ProfileServiceImpl profileServiceImpl;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateMenteeProfileSuccess() {
        // Arrange
        MenteeDTO menteeDTO = new MenteeDTO();
        menteeDTO.setUserName("Mohd ali");
        menteeDTO.setUserMail("ali@example.com");

        // Act
        ResponseEntity<Object> responseEntity = profileController.updateUser(menteeDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Mentee profile updated successfully.", responseBody.get("message"));
        assertEquals("Mentee profile updated", responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testUpdateMentorProfileSuccess() {
        // Arrange
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setUserName("Mohd ali");
        mentorDTO.setUserMail("ali@example.com");

        // Act
        ResponseEntity<Object> responseEntity = profileController.updateUser(mentorDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);


        assertEquals("Mentor profile updated successfully.", responseBody.get("message"));
        assertEquals("Mentor profile updated", responseBody.get("data"));

        // Ensure that the logger.error was never called
        verify(logger, never()).error(anyString());
    }


    @Test
    void testUploadImageSuccess() {

        // Arrange
        String userMail = "user@example.com";
        Picture request = new Picture();
        request.setProfileImage("base64EncodedImageData");

        when(profileServiceImpl.uploadImage(userMail, request.getProfileImage())).thenReturn("ImageUploadedSuccessfully");

        // Act
        ResponseEntity<Object> responseEntity = profileController.uploadImage(userMail, request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Profile image uploaded successfully.", responseBody.get("message"));
        assertEquals("ImageUploadedSuccessfully", responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testUploadImageFailure() {
        // Arrange
        String userMail = "user@example.com";
        Picture request = new Picture();
        request.setProfileImage("base64EncodedImageData");

        when(profileServiceImpl.uploadImage(userMail, request.getProfileImage())).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = profileController.uploadImage(userMail, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to upload profile image.", responseBody.get("error"));


    }

}
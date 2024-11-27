package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.dto.MenteeDTO;
import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.implementation.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class ProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileServiceImpl profileServiceImpl;

    @Test
     void testUploadImage() {
        // Arrange
        String userMail = "test@example.com";
        String base64Image = "base64encodedimagebytes";
        User user = new User();
        user.setUserMail(userMail);

        when(userRepository.findByUserMail(userMail)).thenReturn(user);

        // Act
        String result = profileServiceImpl.uploadImage(userMail, base64Image);

        // Assert
        verify(userRepository, times(1)).findByUserMail(userMail);
        verify(userRepository, times(1)).save(user);
        assertEquals("Image uploaded successfully", result);
    }

    @Test
     void testUploadImageUserNotFound() {
        // Arrange
        String userMail = "nonexistent@example.com";
        String base64Image = "base64encodedimagebytes";

        when(userRepository.findByUserMail(userMail)).thenReturn(null);

        // Act
        String result = profileServiceImpl.uploadImage(userMail, base64Image);

        // Assert
        verify(userRepository, times(1)).findByUserMail(userMail);
        verify(userRepository, never()).save(any(User.class));
        assertEquals("Something went wrong", result);
    }

   @Test
   void testUploadImages_InvalidBase64() {
      String userMail = "test@example.com";
      String invalidBase64Image = "This is not a valid Base64 string";

      assertThrows(IllegalArgumentException.class, () -> profileServiceImpl.uploadImage(userMail, invalidBase64Image));

      verify(userRepository, never()).save(any(User.class));
   }

    @Test
    void testUpdateMenteeProfile() {
        // Create a sample MenteeDTO
        MenteeDTO menteeDTO = new MenteeDTO();
        menteeDTO.setUserMail("test@example.com");
        menteeDTO.setBio("Updated Bio");
        menteeDTO.setMentoringRequiredFor("Updated Mentoring Area");
        // Set other properties as needed

        // Create a sample User entity
        User existingUser = new User();
        existingUser.setUserMail("test@example.com");
        // Set other properties of the existing user as needed

        // Mock the UserRepository to return the existing user when findByUserMail is called
        when(userRepository.findByUserMail("test@example.com")).thenReturn(existingUser);

        // Mock the save method of the UserRepository to return the saved user
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Call the updateMenteeProfile method
        profileServiceImpl.updateMenteeProfile(menteeDTO);

        // Verify that the user has been updated correctly
        assertEquals("Updated Bio", existingUser.getBio());
        assertEquals("Updated Mentoring Area", existingUser.getMentoringRequiredFor());
        // Verify that the save method was called once
        verify(userRepository, times(1)).save(existingUser);
    }


    @Test
    void testUpdateMentorProfile() {
        // Create a sample MentorDTO
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setUserMail("test@example.com");
        mentorDTO.setBio("Updated Bio");
        mentorDTO.setExpertise("Updated Expertise");
        mentorDTO.setIndustry("Updated Industry");
        // Set other properties as needed

        // Create a sample User entity
        User existingUser = new User();
        existingUser.setUserMail("test@example.com");
        // Set other properties of the existing user as needed

        // Mock the UserRepository to return the existing user when findByUserMail is called
        when(userRepository.findByUserMail("test@example.com")).thenReturn(existingUser);

        // Mock the save method of the UserRepository to return the saved user
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        profileServiceImpl.updateMentorProfile(mentorDTO);

        // Verify that the user has been updated correctly
        assertEquals("Updated Bio", existingUser.getBio());
        assertEquals("Updated Expertise", existingUser.getExpertise());
        assertEquals("Updated Industry", existingUser.getIndustry());
        // Verify that the save method was called once
        verify(userRepository, times(1)).save(existingUser);
    }


    @Test
    void testUpdateUserConditionCovered() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User updatedUser = profileServiceImpl.updateUser(user);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(1L, updatedUser.getId().longValue());
        verify(userRepository, times(1)).save(user);
    }

    @Test
     void testGetUserByMailUserFound() {
        // Arrange
        String userMail = "test@example.com";
        User user = new User();
        user.setUserMail(userMail);

        // Mock userRepository.findByUserMail to return the user
        when(userRepository.findByUserMail(userMail)).thenReturn(user);

        // Act
        User retrievedUser = profileServiceImpl.getUserByMail(userMail);

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(userMail, retrievedUser.getUserMail());
        verify(userRepository, times(1)).findByUserMail(userMail);
    }

    @Test
     void testGetUserByMailUserNotFound() {
        // Arrange
        String userMail = "nonexistent@example.com";

        // Mock userRepository.findByUserMail to return null
        when(userRepository.findByUserMail(userMail)).thenReturn(null);

        // Act
        User retrievedUser = profileServiceImpl.getUserByMail(userMail);

        // Assert
        assertNull(retrievedUser);
        verify(userRepository, times(1)).findByUserMail(userMail);
    }


}

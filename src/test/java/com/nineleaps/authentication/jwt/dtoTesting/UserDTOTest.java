package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

 class UserDTOTest {

    @Test
    void testNoArgsConstructor() {
        UserDTO userDTO = new UserDTO();

        assertNull(userDTO.getId());
        assertNull(userDTO.getUserName());
        assertNull(userDTO.getUserMail());
        assertNull(userDTO.getPhoneNumber());
        assertNull(userDTO.getProfileImage());
        assertNull(userDTO.getExpertise());
        assertNull(userDTO.getLocation());
        assertNull(userDTO.getBio());
        assertNull(userDTO.getIndustry());
        assertNull(userDTO.getMentoringRequiredFor());
        assertEquals(0.0, userDTO.getChargePerHour(), 0.001);
        assertNull(userDTO.getAverageRating());
    }

    @Test
    void testGettersAndSetters() {
        UserDTO userDTO = new UserDTO();

        Long id = 1L;
        String userName = "hadi";
        String userMail = "hadi@gmail.com";
        String phoneNumber = "1234567890";
        byte[] profileImage = new byte[]{1, 2, 3};
        String expertise = "Java Developer";
        String location = "India";
        String bio = "Experienced software engineer";
        String industry = "IT";
        String mentoringRequiredFor = "Programming";
        double chargePerHour = 50.0;
        Double averageRating = 4.5;

        userDTO.setId(id);
        userDTO.setUserName(userName);
        userDTO.setUserMail(userMail);
        userDTO.setPhoneNumber(phoneNumber);
        userDTO.setProfileImage(profileImage);
        userDTO.setExpertise(expertise);
        userDTO.setLocation(location);
        userDTO.setBio(bio);
        userDTO.setIndustry(industry);
        userDTO.setMentoringRequiredFor(mentoringRequiredFor);
        userDTO.setChargePerHour(chargePerHour);
        userDTO.setAverageRating(averageRating);

        assertEquals(id, userDTO.getId());
        assertEquals(userName, userDTO.getUserName());
        assertEquals(userMail, userDTO.getUserMail());
        assertEquals(phoneNumber, userDTO.getPhoneNumber());
        assertArrayEquals(profileImage, userDTO.getProfileImage());
        assertEquals(expertise, userDTO.getExpertise());
        assertEquals(location, userDTO.getLocation());
        assertEquals(bio, userDTO.getBio());
        assertEquals(industry, userDTO.getIndustry());
        assertEquals(mentoringRequiredFor, userDTO.getMentoringRequiredFor());
        assertEquals(chargePerHour, userDTO.getChargePerHour(), 0.001);
        assertEquals(averageRating, userDTO.getAverageRating());
    }
}

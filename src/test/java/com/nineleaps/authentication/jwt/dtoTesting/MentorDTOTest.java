package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

 class MentorDTOTest {

    @Test
    void testNoArgsConstructor() {
        MentorDTO mentorDTO = new MentorDTO();

        assertNull(mentorDTO.getId());
        assertNull(mentorDTO.getRoles());
        assertNull(mentorDTO.getUserName());
        assertNull(mentorDTO.getUserMail());
        assertNull(mentorDTO.getPhoneNumber());
        assertNull(mentorDTO.getProfileImage());
        assertNull(mentorDTO.getExpertise());
        assertNull(mentorDTO.getLocation());
        assertNull(mentorDTO.getBio());
        assertNull(mentorDTO.getIndustry());
        assertEquals(0.0, mentorDTO.getChargePerHour());
        assertNull(mentorDTO.getAverageMentorRating());
        assertNull(mentorDTO.getAverageMenteeRating());
    }

    @Test
    void testGettersAndSetters() {
        MentorDTO mentorDTO = new MentorDTO();

        Long id = 1L;
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.MENTOR);
        String userName = "mentor_user";
        String userMail = "mentor@example.com";
        String phoneNumber = "+1234567890";
        byte[] profileImage = new byte[]{0x01, 0x02, 0x03};
        String expertise = "Java Programming";
        String location = "Delhi";
        String bio = "A brief bio";
        String industry = "Information Technology";
        double chargePerHour = 50.0;
        Double averageMentorRating = 4.5;
        Double averageMenteeRating = 4.0;

        mentorDTO.setId(id);
        mentorDTO.setRoles(roles);
        mentorDTO.setUserName(userName);
        mentorDTO.setUserMail(userMail);
        mentorDTO.setPhoneNumber(phoneNumber);
        mentorDTO.setProfileImage(profileImage);
        mentorDTO.setExpertise(expertise);
        mentorDTO.setLocation(location);
        mentorDTO.setBio(bio);
        mentorDTO.setIndustry(industry);
        mentorDTO.setChargePerHour(chargePerHour);
        mentorDTO.setAverageMentorRating(averageMentorRating);
        mentorDTO.setAverageMenteeRating(averageMenteeRating);

        assertEquals(id, mentorDTO.getId());
        assertEquals(roles, mentorDTO.getRoles());
        assertEquals(userName, mentorDTO.getUserName());
        assertEquals(userMail, mentorDTO.getUserMail());
        assertEquals(phoneNumber, mentorDTO.getPhoneNumber());
        assertArrayEquals(profileImage, mentorDTO.getProfileImage());
        assertEquals(expertise, mentorDTO.getExpertise());
        assertEquals(location, mentorDTO.getLocation());
        assertEquals(bio, mentorDTO.getBio());
        assertEquals(industry, mentorDTO.getIndustry());
        assertEquals(chargePerHour, mentorDTO.getChargePerHour());
        assertEquals(averageMentorRating, mentorDTO.getAverageMentorRating());
        assertEquals(averageMenteeRating, mentorDTO.getAverageMenteeRating());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        Set<UserRole> roles = Set.of(UserRole.MENTOR);
        String userName = "hadi";
        String userMail = "hadi@gmail.com";
        String phoneNumber = "1234567890";
        byte[] profileImage = new byte[0];
        String expertise = "Java Programming";
        String location = "India";
        String bio = "Experienced Java developer";
        String industry = "Software Development";
        double chargePerHour = 50.0;
        Double averageMentorRating = 4.5;
        Double averageMenteeRating = 4.7;

        MentorDTO mentorDTO = new MentorDTO(id, roles, userName, userMail, phoneNumber, profileImage,
                expertise, location, bio, industry, chargePerHour, averageMentorRating, averageMenteeRating);

        assertNotNull(mentorDTO);
    }
}

package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.MenteeDTO;
import com.nineleaps.authentication.jwt.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class MenteeDTOTest {

    @Test
    void testNoArgsConstructor() {
        MenteeDTO menteeDTO = new MenteeDTO();

        assertNull(menteeDTO.getId());
        assertNull(menteeDTO.getRoles());
        assertNull(menteeDTO.getUserName());
        assertNull(menteeDTO.getUserMail());
        assertNull(menteeDTO.getPhoneNumber());
        assertNull(menteeDTO.getLocation());
        assertNull(menteeDTO.getBio());
        assertNull(menteeDTO.getMentoringRequiredFor());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.MENTEE);
        String userName = "mentee_user";
        String userMail = "mentee@example.com";
        String phoneNumber = "+1234567890";
        String location = "New York";
        String bio = "A brief bio";
        String mentoringRequiredFor = "Career Development";

        MenteeDTO menteeDTO = new MenteeDTO(
                id,
                roles,
                userName,
                userMail,
                phoneNumber,
                location,
                bio,
                mentoringRequiredFor
        );

        assertEquals(id, menteeDTO.getId());
        assertEquals(roles, menteeDTO.getRoles());
        assertEquals(userName, menteeDTO.getUserName());
        assertEquals(userMail, menteeDTO.getUserMail());
        assertEquals(phoneNumber, menteeDTO.getPhoneNumber());
        assertEquals(location, menteeDTO.getLocation());
        assertEquals(bio, menteeDTO.getBio());
        assertEquals(mentoringRequiredFor, menteeDTO.getMentoringRequiredFor());
    }

    @Test
    void testGettersAndSetters() {
        MenteeDTO menteeDTO = new MenteeDTO();

        Long id = 1L;
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.MENTEE);
        String userName = "mentee_user";
        String userMail = "mentee@example.com";
        String phoneNumber = "+1234567890";
        String location = "New York";
        String bio = "A brief bio";
        String mentoringRequiredFor = "Career Development";

        menteeDTO.setId(id);
        menteeDTO.setRoles(roles);
        menteeDTO.setUserName(userName);
        menteeDTO.setUserMail(userMail);
        menteeDTO.setPhoneNumber(phoneNumber);
        menteeDTO.setLocation(location);
        menteeDTO.setBio(bio);
        menteeDTO.setMentoringRequiredFor(mentoringRequiredFor);

        assertEquals(id, menteeDTO.getId());
        assertEquals(roles, menteeDTO.getRoles());
        assertEquals(userName, menteeDTO.getUserName());
        assertEquals(userMail, menteeDTO.getUserMail());
        assertEquals(phoneNumber, menteeDTO.getPhoneNumber());
        assertEquals(location, menteeDTO.getLocation());
        assertEquals(bio, menteeDTO.getBio());
        assertEquals(mentoringRequiredFor, menteeDTO.getMentoringRequiredFor());
    }
}

package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserAttributesAndRoles() {
        Long id = 1L;
        String userName = "hadi";
        String userMail = "hadi@example.com";
        String phoneNumber = "1234567890";
        String userPassword = "password";
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.MENTEE);
        roles.add(UserRole.MENTOR);
        byte[] profileImage = new byte[]{1, 2, 3};
        String expertise = "Software Development";
        String location = "City, Country";
        String bio = "I am a software developer.";
        String industry = "Technology";
        String mentoringRequiredFor = "Java Programming";
        double chargePerHour = 50.0;

        User user = new User(id, userName, userMail, phoneNumber, userPassword, roles, profileImage, expertise, location, bio, industry, mentoringRequiredFor, chargePerHour);

        assertEquals(id, user.getId());
        assertEquals(userName, user.getUserName());
        assertEquals(userMail, user.getUserMail());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(userPassword, user.getUserPassword());
        assertEquals(roles, user.getRoles());
        assertArrayEquals(profileImage, user.getProfileImage());
        assertEquals(expertise, user.getExpertise());
        assertEquals(location, user.getLocation());
        assertEquals(bio, user.getBio());
        assertEquals(industry, user.getIndustry());
        assertEquals(mentoringRequiredFor, user.getMentoringRequiredFor());
        assertEquals(chargePerHour, user.getChargePerHour(), 0.01);
    }

    @Test
    void testUserConstructors() {
        String userMail = "hadi@example.com";
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.MENTEE);

        User userWithEmailAndRoles = new User(userMail, roles);

        assertEquals(userMail, userWithEmailAndRoles.getUserMail());
        assertEquals(roles, userWithEmailAndRoles.getRoles());

        String userName = "John";
        User userWithUserNameAndRoles = new User(userName, userMail, roles);

        assertEquals(userName, userWithUserNameAndRoles.getUserName());
        assertEquals(userMail, userWithUserNameAndRoles.getUserMail());
        assertEquals(roles, userWithUserNameAndRoles.getRoles());
    }
    // Test the NoArgsConstructor

    @Test
    void testNoArgsConstructor() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getUserName());
        assertNull(user.getUserMail());
        assertNull(user.getPhoneNumber());
        assertNull(user.getUserPassword());
        assertNull(user.getRoles());
        assertNull(user.getProfileImage());
        assertNull(user.getExpertise());
        assertNull(user.getLocation());
        assertNull(user.getBio());
        assertNull(user.getIndustry());
        assertNull(user.getMentoringRequiredFor());
        assertEquals(0.0, user.getChargePerHour(), 0.01);
}}

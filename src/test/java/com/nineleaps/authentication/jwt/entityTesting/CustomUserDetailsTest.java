package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_USER");
        customUserDetails = new CustomUserDetails("hadi@exm.com", "password", authorities);
    }

    @Test
    void testGettersAndSetters() {
        customUserDetails.setUserId(1L);

        assertEquals(1L, customUserDetails.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomUserDetails anotherUserDetails = new CustomUserDetails("test@hadi@exm.com", "password", new ArrayList<>());
        customUserDetails.setUserId(1L);
        anotherUserDetails.setUserId(1L);

        assertEquals(customUserDetails, anotherUserDetails);
        assertEquals(customUserDetails.hashCode(), anotherUserDetails.hashCode());

        customUserDetails.setUserId(2L);

        assertNotEquals(customUserDetails, anotherUserDetails);
        assertNotEquals(customUserDetails.hashCode(), anotherUserDetails.hashCode());
    }

    @Test
    void testConstructorAndBuildAuthoritiesSet() {
        String userMail = "test@hadi@exm.com";

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_USER");

        CustomUserDetails customUserDetails = new CustomUserDetails(userMail, authorities);

        assertEquals(userMail, customUserDetails.getUsername());
        assertEquals(userMail, customUserDetails.getPassword());

        Set<GrantedAuthority> expectedAuthorities = new HashSet<>(authorities);
        assertEquals(expectedAuthorities, customUserDetails.getAuthorities());
    }

    @Test
    void testHashCode() {
        Long userId = 123L;
        CustomUserDetails customUserDetails = new CustomUserDetails("test@hadi@exm.com", Collections.emptyList());
        customUserDetails.setUserId(userId);

        int expectedHashCode = userId.hashCode();
        int actualHashCode = customUserDetails.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    void testHashCodeWithNullUserId() {
        CustomUserDetails customUserDetails = new CustomUserDetails("hadi@exm.com", Collections.emptyList());

        int expectedHashCode = 0;
        int actualHashCode = customUserDetails.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    void testEqualsWithSameObject() {
        CustomUserDetails userDetails = new CustomUserDetails("test@example.com", Collections.emptyList());
        assertEquals(userDetails, userDetails);
    }

    @Test
    void testEqualsWithNullObject() {
        CustomUserDetails userDetails = new CustomUserDetails("test@example.com", Collections.emptyList());
        assertNotEquals(null, userDetails);
    }

    @Test
    void testEqualsWithDifferentClass() {
        CustomUserDetails userDetails = new CustomUserDetails("test@example.com", Collections.emptyList());
        assertNotEquals("NotCustomUserDetails", userDetails);
    }

    @Test
    void testEqualsWithSameUserId() {
        Long userId = 123L;
        CustomUserDetails userDetails1 = new CustomUserDetails("test1@example.com", Collections.emptyList());
        userDetails1.setUserId(userId);

        CustomUserDetails userDetails2 = new CustomUserDetails("test2@example.com", Collections.emptyList());
        userDetails2.setUserId(userId);

        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithDifferentUserId() {
        Long userId1 = 123L;
        Long userId2 = 456L;

        CustomUserDetails userDetails1 = new CustomUserDetails("test1@example.com", Collections.emptyList());
        userDetails1.setUserId(userId1);

        CustomUserDetails userDetails2 = new CustomUserDetails("test2@example.com", Collections.emptyList());
        userDetails2.setUserId(userId2);

        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithNullUserId() {
        CustomUserDetails userDetails1 = new CustomUserDetails("test1@example.com", Collections.emptyList());
        CustomUserDetails userDetails2 = new CustomUserDetails("test2@example.com", Collections.emptyList());

        assertEquals(userDetails1, userDetails2);
    }



    @Test
    void testEqualsWithBothNullUserId() {
        CustomUserDetails userDetails1 = new CustomUserDetails("test1@example.com", Collections.emptyList());
        userDetails1.setUserId(null);

        CustomUserDetails userDetails2 = new CustomUserDetails("test2@example.com", Collections.emptyList());
        userDetails2.setUserId(null);

        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithNullAndNonNullUserId() {
        CustomUserDetails userDetails1 = new CustomUserDetails("test1@example.com", Collections.emptyList());
        userDetails1.setUserId(null);

        CustomUserDetails userDetails2 = new CustomUserDetails("test2@example.com", Collections.emptyList());
        userDetails2.setUserId(123L);

        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithNonNullAndNullUserId() {
        CustomUserDetails userDetails1 = new CustomUserDetails("test1@example.com", Collections.emptyList());
        userDetails1.setUserId(123L);

        CustomUserDetails userDetails2 = new CustomUserDetails("test2@example.com", Collections.emptyList());
        userDetails2.setUserId(null);

        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    void testEqualsWithNonNullAndEqualUserId() {
        Long userId = 123L;

        CustomUserDetails userDetails1 = new CustomUserDetails("test1@example.com", Collections.emptyList());
        userDetails1.setUserId(userId);

        CustomUserDetails userDetails2 = new CustomUserDetails("test2@example.com", Collections.emptyList());
        userDetails2.setUserId(userId);

        assertEquals(userDetails1, userDetails2);
    }


    @Test
    void testEqualsWithDifferentClasses() {
        CustomUserDetails userDetails = new CustomUserDetails("test@example.com", Collections.emptyList());
        Object otherObject = new Object(); // Creating an object of a different class

        assertNotEquals(userDetails, otherObject);
    }


    @Test
    void testEqualsOneNullOneNotNull() {
        CustomUserDetails userDetails = new CustomUserDetails("test@example.com", Collections.emptyList());
        CustomUserDetails nullUserDetails = null;
        assertNotEquals(userDetails, nullUserDetails);
    }


    @Test
    void testEqualsBothNull() {
        CustomUserDetails userDetails1 = null;
        CustomUserDetails userDetails2 = null;
        assertEquals(userDetails1, userDetails2);
    }



}
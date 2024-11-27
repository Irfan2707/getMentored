package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.SSOUserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class SSOUserDtoTest {

    @Test
    void testNoArgsConstructor() {
        SSOUserDto ssoUserDto = new SSOUserDto();

        assertNull(ssoUserDto.getId());
        assertNull(ssoUserDto.getUserMail());
        assertNull(ssoUserDto.getUserName());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String userMail = "hadi@example.com";
        String userName = "hadi";

        SSOUserDto ssoUserDto = new SSOUserDto(id, userMail, userName);

        assertEquals(id, ssoUserDto.getId());
        assertEquals(userMail, ssoUserDto.getUserMail());
        assertEquals(userName, ssoUserDto.getUserName());
    }

    @Test
    void testGettersAndSetters() {
        SSOUserDto ssoUserDto = new SSOUserDto();

        Long id = 1L;
        String userMail = "hadi@example.com";
        String userName = "hadi";

        ssoUserDto.setId(id);
        ssoUserDto.setUserMail(userMail);
        ssoUserDto.setUserName(userName);

        assertEquals(id, ssoUserDto.getId());
        assertEquals(userMail, ssoUserDto.getUserMail());
        assertEquals(userName, ssoUserDto.getUserName());
    }
}

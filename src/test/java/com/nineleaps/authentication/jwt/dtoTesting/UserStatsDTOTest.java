package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.UserStatsDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class UserStatsDTOTest {

    @Test
    void testAllArgsConstructor() {
        Long totalUsers = 100L;
        Long menteesCount = 40L;
        Long mentorsCount = 60L;

        UserStatsDTO userStatsDTO = new UserStatsDTO(totalUsers, menteesCount, mentorsCount);

        assertEquals(totalUsers, userStatsDTO.getTotalUsers());
        assertEquals(menteesCount, userStatsDTO.getMenteesCount());
        assertEquals(mentorsCount, userStatsDTO.getMentorsCount());
    }

    @Test
    void testGettersAndSetters() {
        UserStatsDTO userStatsDTO = new UserStatsDTO();

        Long totalUsers = 100L;
        Long menteesCount = 40L;
        Long mentorsCount = 60L;

        userStatsDTO.setTotalUsers(totalUsers);
        userStatsDTO.setMenteesCount(menteesCount);
        userStatsDTO.setMentorsCount(mentorsCount);

        assertEquals(totalUsers, userStatsDTO.getTotalUsers());
        assertEquals(menteesCount, userStatsDTO.getMenteesCount());
        assertEquals(mentorsCount, userStatsDTO.getMentorsCount());
    }
}

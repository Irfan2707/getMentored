package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.dto.EngagementStatisticsDTO;
import com.nineleaps.authentication.jwt.entity.ConnectionRequest;
import com.nineleaps.authentication.jwt.entity.Engagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface EngagementRepository extends JpaRepository<Engagement, Long> {
    boolean existsByStartTimeAndDurationHours(LocalDateTime startTime, int durationHours);

    boolean existsByConnectionRequestId(Long connectionRequestId);

    List<Engagement> findByConnectionRequest(ConnectionRequest connectionRequest);


    @Query("SELECT " +
            "e.id AS engagementId, " +
            "e.durationHours AS durationHours, " +
            "e.startTime AS startTime, " +
            "c.mentee.id AS menteeId, " +
            "c.mentor.id AS mentorId, " +
            "c.mentee.userName AS menteeName, " +
            "c.mentor.userName AS mentorName, " +
            "c.mentor.profileImage AS mentorProfileImage, " +
            "c.mentee.profileImage AS menteeProfileImage, " +
            "es.completedEngStatusTimestamp AS completedEngStatusTime " +

            "FROM Engagement e " +
            "JOIN e.connectionRequest c " +
            "JOIN c.mentor m " +
            "LEFT JOIN e.engagementStatuses es " +
            "WHERE c.mentee.id = :userId OR c.mentor.id = :userId")
    List<Map<String, Object>> findEngagementDetailsByUserId(@Param("userId") Long userId);


    //	_________________________
//	Counting of Total engagements/average of hours spent and total hours

    @Query("SELECT new com.nineleaps.authentication.jwt.dto.EngagementStatisticsDTO(" +
            "COUNT(e) AS totalEngagements , " +
            "SUM(e.durationHours) AS totalHours, " +
            "AVG(e.durationHours) AS averageHours) " +
            "FROM Engagement e")
    EngagementStatisticsDTO getEngagementSummary();

//	________________________


}




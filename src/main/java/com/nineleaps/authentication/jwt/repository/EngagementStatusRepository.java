package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.entity.EngagementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository

public interface EngagementStatusRepository extends JpaRepository<EngagementStatus, Long> {
    boolean existsByEngagementId(Long engagementId);

    String JPA_QUERY = "SELECT COUNT(eng), " +
            "SUM(CASE WHEN es.menteeEngStatus = 'PENDING' OR es.mentorEngStatus = 'PENDING' THEN 1 ELSE 0 END) As pengingEngagements, " +
            "SUM(CASE WHEN es.menteeEngStatus = 'DONE' AND es.mentorEngStatus = 'DONE' THEN 1 ELSE 0 END)AS completedEngagements " +
            "FROM Engagement eng " +
            "JOIN eng.engagementStatuses es";

    Optional<EngagementStatus> findByEngagementId(Long engagementId);
}

package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.entity.GoalTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalTrackerRepository extends JpaRepository<GoalTracker, Long> {


    boolean existsByDescriptionAndEngagementId(String description, Long engagementId);

    GoalTracker findByEngagementId(Long engagementId);

    List<GoalTracker> findAllByEngagementId(Long engagementId);

    //	Counting no of goals for an engagement
    @Query("SELECT COUNT(g) FROM GoalTracker g WHERE g.engagement.id = :engagementId")
    int countGoalsByEngagement(@Param("engagementId") Long engagementId);

}

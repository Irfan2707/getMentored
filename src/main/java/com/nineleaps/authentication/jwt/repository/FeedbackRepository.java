package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.dto.FeedbackAnalyticsDTO;
import com.nineleaps.authentication.jwt.entity.Feedback;
import com.nineleaps.authentication.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByMentor(User mentor);

    List<Feedback> findByMentee(User mentee);

    List<Feedback> findByMentorId(Long mentorId);

    List<Feedback> findByMenteeId(Long menteeId);

    boolean existsByMenteeIdAndMentorIdAndEngagementId(Long menteeId, Long mentorId, Long engagementId);

    boolean existsByMenteeIdAndMentorIdAndEngagementIdAndMentorRating(Long menteeId, Long mentorId, Long engagementId, Double rating);

    //Query part
    List<Feedback> findAllByMenteeId(Long menteeId);

    List<Feedback> findAllByMentorId(Long mentorId);

    List<Feedback> findAllByEngagementId(Long engagementId);

    List<Feedback> findAllByMentorIdOrMenteeId(Long userId, Long userrId);

    @Query("SELECT AVG(f.mentorRating) FROM Feedback f WHERE f.mentor.id = :mentorId")
    Double calculateAverageRatingForMentorId(@Param("mentorId") Long mentorId);

    @Query("SELECT AVG(f.menteeRating) FROM Feedback f WHERE f.mentee.id = :menteeId")
    Double calculateAverageRatingForMenteeId(@Param("menteeId") Long menteeId);

    @Query("SELECT AVG(f.engagementRating) FROM Feedback f WHERE f.engagement.id = :engagementId")
    Double calculateAverageRatingForEngagementId(@Param("engagementId") Long engagementId);

    @Query("SELECT AVG(f.engagementRating) FROM Feedback f WHERE f.mentor.id = :mentorid")
    Double calculateAverageRatingForEngagement(@Param("mentorid") Long mentorid);

    //        ____________________________________________________-
//        Feedback counting

    @Query("SELECT " +
            "NEW com.nineleaps.authentication.jwt.dto.FeedbackAnalyticsDTO(" +
            "AVG(f.engagementRating), " +
            "AVG(f.menteeRating), " +
            "AVG(f.mentorRating), " +
            "COUNT(CASE WHEN f.engagementRating > 3 THEN 1 END), " +
            "COUNT(CASE WHEN f.engagementRating < 3 THEN 1 END), " +
            "COUNT(CASE WHEN f.engagementRating = 3 THEN 1 END)) " +
            "FROM Feedback f")
    FeedbackAnalyticsDTO getFeedbackAnalytics();


//    ________________________________________________


}

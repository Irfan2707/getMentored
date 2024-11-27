package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.dto.ConnectionRequestStatisticsReportDTO;
import com.nineleaps.authentication.jwt.entity.ConnectionRequest;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRequestRepo extends JpaRepository<ConnectionRequest, Long> {
    ConnectionRequest findByMenteeAndMentorAndStatusNot(User mentee, User mentor, ConnectionRequestStatus rejected);

    Page<ConnectionRequest> findAllByMenteeIdOrMentorId(Long menteeId, Long mentorId, Pageable pageable);

    List<ConnectionRequest> findAllByMenteeIdOrMentorId(Long menteeId, Long mentorId);

    ConnectionRequest findByMentorAndMentee(User mentor, User mentee);

    boolean existsByMentorAndMentee(User mentor, User mentee);

    @EntityGraph(attributePaths = "recommendedMentors")
    List<ConnectionRequest> findByMenteeId(Long menteeId);

    List<ConnectionRequest> findByMenteeIdOrMentorId(Long userId, Long userId2);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT DISTINCT cr.mentor.id FROM ConnectionRequest cr WHERE cr.mentee.id = :menteeId AND cr.status = 'ACCEPTED')")
    List<User> findUsersWithoutAcceptedConnections(@Param("menteeId") Long menteeId);


    @Query("SELECT COUNT(cr) FROM ConnectionRequest cr WHERE cr.mentee.id = :menteeId")
    int countConnectionRequestsSentByMentee(@Param("menteeId") Long menteeId);

    //Number of requests sent by mentee which are accepted
    @Query("SELECT COUNT(cr) FROM ConnectionRequest cr WHERE cr.mentee.id = :menteeId AND cr.status = 'ACCEPTED'")
    int countAcceptedRequestsByMentee(@Param("menteeId") Long menteeId);

    //Number of requests sent by mentee which are rejected
    @Query("SELECT COUNT(cr) FROM ConnectionRequest cr WHERE cr.mentee.id = :menteeId AND cr.status = 'REJECTED'")
    int countRejectedRequestsByMentee(@Param("menteeId") Long menteeId);

    //Number of requests sent by mentee which are pending
    @Query("SELECT COUNT(cr) FROM ConnectionRequest cr WHERE cr.mentee.id = :menteeId AND cr.status = 'PENDING'")
    int countPendingRequestsByMentee(@Param("menteeId") Long menteeId);

    //No of requests recieved by mentor
    @Query("SELECT COUNT(cr) FROM ConnectionRequest cr WHERE cr.mentor.id = :mentorId")
    int countConnectionRequestsReceivedByMentor(@Param("mentorId") Long mentorId);

    // Number of connection requests accepted for a mentor
    @Query("SELECT COUNT(cr) FROM ConnectionRequest cr WHERE cr.mentor.id = :mentorId AND cr.status = 'ACCEPTED'")
    int countConnectionRequestsAcceptedByMentor(@Param("mentorId") Long mentorId);

    // Number of connection requests rejected for a mentor
    @Query("SELECT COUNT(cr) FROM ConnectionRequest cr WHERE cr.mentor.id = :mentorId AND cr.status = 'REJECTED'")
    int countConnectionRequestsRejectedByMentor(@Param("mentorId") Long mentorId);

    //Number of connection requests pending for a mentor
    @Query("SELECT COUNT(cr) FROM ConnectionRequest cr WHERE cr.mentor.id = :mentorId AND cr.status = 'PENDING'")
    int countConnectionRequestsPendingByMentor(@Param("mentorId") Long mentorId);
// _________________________________________________________________________________________
//Count the total no request sent out of them how many are pending, accepted and rejected

    @Query("SELECT new com.nineleaps.authentication.jwt.dto.ConnectionRequestStatisticsReportDTO(" +
            "COUNT(cr) , " +
            "SUM(CASE WHEN cr.status = 'ACCEPTED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN cr.status = 'REJECTED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN cr.status = 'PENDING' THEN 1 ELSE 0 END)) " +
            "FROM ConnectionRequest cr")
    ConnectionRequestStatisticsReportDTO getConnectionRequestStatistics();

    //	_______________________________________________________________________________________________
    ConnectionRequest findByMenteeIdAndMentorId(Long menteeId, Long mentorId);

    @Query("SELECT cr.mentee.userName AS menteeName, cr.mentor.userName AS mentorName, cr.mentee.id AS menteeId, cr.mentor.id AS mentorId, cr.message " +
            "FROM ConnectionRequest cr " +
            //"WHERE cr.mentee.id = :userId OR cr.mentor.id = :userId")
            "WHERE (cr.mentee.id = :userId OR cr.mentor.id = :userId) AND cr.status = 'PENDING'")
    List<Object[]> findConnectionDetailsByUserId(@Param("userId") Long userId);

    boolean existsByMentorAndMenteeIdAndStatusIn(User mentor, Long menteeId, ConnectionRequestStatus[] statuses);


    @Query("SELECT u.userName, c.id, c.message, c.mentee.id, c.mentor.id, u.mentoringRequiredFor, u.location FROM ConnectionRequest c JOIN c.mentee u WHERE c.mentor.id = :mentorId AND c.status = 'PENDING'")
    List<Object[]> findConnectionsByMenteeId(@Param("mentorId") Long mentorId);
}
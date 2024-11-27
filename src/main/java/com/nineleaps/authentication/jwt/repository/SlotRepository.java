package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.entity.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

    boolean existsByStartDateTimeAndEndDateTimeAndMentorId(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                           Long mentorId);

    boolean existsById(Long slotId);

    //Queryfor counting the slots

    @Query("SELECT COUNT(s) FROM Slot s WHERE s.mentorId = :mentorId")
    int getTotalSlotsByMentorId(Long mentorId);

    @Query("SELECT COUNT(s) FROM Slot s WHERE s.mentorId = :mentorId AND s.status = 'BOOKED'")
    int getBookedSlotsByMentorId(Long mentorId);

    @Query("SELECT COUNT(s) FROM Slot s WHERE s.mentorId = :mentorId AND s.status = 'PENDING'")
    int getPendingSlotsByMentorId(Long mentorId);

    //		___________________________________________________-
//		Queries for reporting
    long count();

    @Query("SELECT s.status, COUNT(s) FROM Slot s GROUP BY s.status")
    List<Object[]> countByStatus();

    @Query("SELECT s.mentorId, COUNT(s) FROM Slot s GROUP BY s.mentorId")
    List<Object[]> countByMentor();

    @Query(value = "SELECT TIMESTAMPDIFF(HOUR, s.start_date_time, s.end_date_time) FROM Slot s", nativeQuery = true)
    List<Long> getDurations();

    @Query("SELECT DATE(s.createdAt), COUNT(s) FROM Slot s GROUP BY DATE(s.createdAt)")
    List<Object[]> getFrequency();

    //		__________________________________________________________
    Page<Slot> findByMentorIdAndStartDateTimeAfter(Long mentorId, LocalDateTime startDateTime, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Slot> findById(Long id);


    List<Slot> findByMentorIdAndStartDateTimeGreaterThanEqualAndStartDateTimeLessThanEqual(
            Long mentorId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Slot> findByMentorIdAndStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(Long mentorId,
                                                                                         LocalDateTime overlappingSlotEnd, LocalDateTime overlappingSlotStart);


    @Query("SELECT COUNT(s) FROM Slot s " +
            "WHERE s.mentorId = :mentorId " +
            "AND s.createdAt BETWEEN :startDate AND :endDate")
    long getTotalSlotsByMentorAndDateRange(@Param("mentorId") Long mentorId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) FROM Slot s " +
            "WHERE s.mentorId = :mentorId " +
            "AND s.createdAt BETWEEN :startDate AND :endDate " +
            "AND s.status = 'PENDING'")
    long getPendingSlotsByMentorAndDateRange(@Param("mentorId") Long mentorId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) FROM Slot s " +
            "WHERE s.mentorId = :mentorId " +
            "AND s.createdAt BETWEEN :startDate AND :endDate " +
            "AND s.status = 'BOOKED'")
    long getBookedSlotsByMentorAndDateRange(@Param("mentorId") Long mentorId,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);
}
















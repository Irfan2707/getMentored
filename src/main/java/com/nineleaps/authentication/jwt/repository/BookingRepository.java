package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.entity.Booking;
import com.nineleaps.authentication.jwt.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findBySlotAndBookingDateTime(Slot slot, LocalDateTime bookingDateTime);

    @Query("SELECT " +
            "b.id AS bookingId, " +
            "b.bookingDateTime AS bookingDateTime, " +
            "b.slot.startDateTime AS slotStartDateTime, " +
            "b.slot.endDateTime AS slotEndDateTime, " +
            "b.engagement.id AS engagementId, " +
            "b.noOfHours AS noOfHours, " +
            "b.mentee.id AS menteeId, " +
            "b.mentor.id AS mentorId, " +
            "b.mentee.userName AS menteeName, " +
            "b.mentor.userName AS mentorName " +
            "FROM Booking b " +
            "WHERE (b.mentee.id = :userId OR b.mentor.id = :userId) " +
            "AND b.slot.startDateTime >= CURRENT_DATE")
    List<Map<String, Object>> findBookingDetailsByUserId(@Param("userId") Long userId);


    boolean existsBySlotId(Long slotId);

    List<Booking> findBySlotId(Long id);


}




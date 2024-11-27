package com.nineleaps.authentication.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking", indexes = {
        @Index(name = "idx_mentee_id", columnList = "mentee_id"),
        @Index(name = "idx_mentor_id", columnList = "mentor_id"),
        @Index(name = "idx_slot_id", columnList = "slot_id"),
        @Index(name = "idx_booking_date_time", columnList = "booking_date_time")
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_date_time", nullable = false)
    private LocalDateTime bookingDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentee_id", referencedColumnName = "id")
    private User mentee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", referencedColumnName = "id")
    private User mentor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "engagement_id")
    private Engagement engagement;

    @Column(name = "no_of_hours", nullable = false)
    private Integer noOfHours;

    @Version
    private Long version;

    @Column(name = "is_deleted")
    private Boolean deleted = false;
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;


}
package com.nineleaps.authentication.jwt.entity;

import com.nineleaps.authentication.jwt.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "slot")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Booking> bookings = new ArrayList<>();


    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setSlot(this);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setSlot(null);
    }

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "mentor_id")
    private Long mentorId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Column(name = "is_deleted")
    private Boolean deleted = false;
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}



package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.BookingDTO;
import com.nineleaps.authentication.jwt.entity.Booking;
import com.nineleaps.authentication.jwt.entity.Slot;
import com.nineleaps.authentication.jwt.enums.BookingStatus;
import com.nineleaps.authentication.jwt.exception.SlotAlreadyExistsException;
import com.nineleaps.authentication.jwt.repository.BookingRepository;
import com.nineleaps.authentication.jwt.repository.EngagementRepository;
import com.nineleaps.authentication.jwt.repository.SlotRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.interfaces.IBookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements IBookingService {
    @Autowired
    private final SlotRepository slotRepository;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final EmailServiceImpl emailService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EngagementRepository engagementRepository;
    public static final String BOOKING_CANCELLATION_NOTIFICATION_SUBJECT = "Booking Cancellation Notification";

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);


    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        try {
            logger.info("Creating a new booking for slot ID: {}", bookingDTO.getSlotId());

            Slot slot = slotRepository.findById(bookingDTO.getSlotId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid slot ID"));

            if (bookingRepository.existsBySlotId(bookingDTO.getSlotId())) {
                throw new SlotAlreadyExistsException("A booking with the same slot already exists.");
            }

            LocalDateTime bookingDateTime = bookingDTO.getBookingDateTime();
            LocalDateTime startDateTime = slot.getStartDateTime();
            LocalDateTime endDateTime = slot.getEndDateTime();
            Duration duration = Duration.between(startDateTime, endDateTime);
            int noOfHours = (int) duration.toHours();

            Booking booking = new Booking();
            booking.setBookingDateTime(bookingDateTime);
            booking.setSlot(slot);

            booking.setCreatedAt(LocalDateTime.now());
            booking.setEngagement(engagementRepository.findById(bookingDTO.getEngagementId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid engagement ID")));
            booking.setMentee(userRepository.findById(bookingDTO.getMenteeId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid mentee ID")));
            booking.setMentor(userRepository.findById(bookingDTO.getMentorId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid mentor ID")));
            booking.setNoOfHours(noOfHours);

            booking.getMentee().setUserName(bookingDTO.getMenteeUsername());
            booking.getMentor().setUserName(bookingDTO.getMentorUsername());

            Booking savedBooking = bookingRepository.save(booking);
            slot.setStatus(BookingStatus.BOOKED);
            slotRepository.save(slot);

            BookingDTO responseDTO = getBookingDTO(savedBooking);

            String mentorEmail = savedBooking.getMentor().getUserMail();
            String menteeName = savedBooking.getMentee().getUserName();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
            String startDateTimeFormatted = startDateTime.format(formatter);
            String endDateTimeFormatted = endDateTime.format(formatter);
            String subject = "Booking Notification";
            String content = "Your Mentee " + menteeName + " has booked your slot from " + startDateTimeFormatted + " to " + endDateTimeFormatted + ".";
            emailService.sendEmail(subject, content, mentorEmail);

            logger.info("Booking created successfully for slot ID: {}", bookingDTO.getSlotId());

            return responseDTO;
        } catch (SlotAlreadyExistsException ex) {

            throw ex; // Re-throw the custom exception
        } catch (OptimisticLockException ex) {
            logger.error("Optimistic lock exception occurred: {}", ex.getMessage());
            throw new SlotAlreadyExistsException("Another user has booked the slot at the same time. Please try again.");
        } catch (Exception ex) {
            logger.error("An error occurred while creating the booking is found: {}", ex.getMessage());
            throw new SlotAlreadyExistsException("An error occurred while creating the booking is found.");
        }
    }


    private static BookingDTO getBookingDTO(Booking savedBooking) {
        BookingDTO responseDTO;
        responseDTO = new BookingDTO();
        responseDTO.setId(savedBooking.getId());
        responseDTO.setBookingDateTime(savedBooking.getBookingDateTime());
        responseDTO.setSlotId(savedBooking.getSlot().getId());
        responseDTO.setMenteeId(savedBooking.getMentee().getId());
        responseDTO.setMenteeUsername(savedBooking.getMentee().getUserName());
        responseDTO.setMentorId(savedBooking.getMentor().getId());
        responseDTO.setMentorUsername(savedBooking.getMentor().getUserName());
        responseDTO.setEngagementId(savedBooking.getEngagement().getId());
        responseDTO.setNoOfHours(savedBooking.getNoOfHours());
        return responseDTO;
    }

    @Override
    public void deleteBooking(Long id) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        logger.info("Deleting booking with id: {}", id);

        sendCancellationEmailToMentor(booking);
        booking.setDeleted(true);
        booking.setUpdatedTime(LocalDateTime.now());
        bookingRepository.save(booking);
        Slot slot = booking.getSlot();

        if (slot != null) {
            slot.getBookings().remove(booking);
            slotRepository.save(slot);
            List<Booking> bookingsForSlot = bookingRepository.findBySlotId(slot.getId());

            if (bookingsForSlot.isEmpty()) {

                slot.setDeleted(true);
                slot.setUpdatedTime(LocalDateTime.now());
                slotRepository.save(slot);
            }
        }
    }


    @Override
    @Transactional
    public void deleteMultipleBookingsById(List<Long> bookingIds) throws ResourceNotFoundException {
        logger.info("Deleting multiple bookings by IDs: {}", bookingIds);

        for (Long bookingId : bookingIds) {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                    () -> new ResourceNotFoundException("Booking not found with ID: " + bookingId)
            );

            Slot slot = booking.getSlot();
            if (slot != null) {
                sendCancellationEmailToMentor(booking);

                // Set the soft delete flags
                booking.setDeleted(true);
                booking.setUpdatedTime(LocalDateTime.now());
                slot.setDeleted(true);
                slot.setUpdatedTime(LocalDateTime.now());

                // Save the updated booking and slot (soft delete)
                bookingRepository.save(booking);
                slotRepository.save(slot);
            }
        }

        logger.info("Multiple bookings with IDs {} soft deleted successfully", bookingIds);
    }


    @Transactional
    public void sendCancellationEmailToMentor(Booking booking) {
        if (booking != null && booking.getSlot() != null && booking.getBookingDateTime() != null) {
            LocalDateTime bookingDateTime = booking.getBookingDateTime();

            // Send email notification to mentor
            String mentorEmail = booking.getMentor().getUserMail();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
            String bookingDateTimeFormatted = bookingDateTime.format(formatter);
            String subject = BOOKING_CANCELLATION_NOTIFICATION_SUBJECT;
            String content = "Your booking scheduled for " + bookingDateTimeFormatted + " has been cancelled.";
            emailService.sendEmail(subject, content, mentorEmail);
        }
    }

    @Override
    public List<Map<String, Object>> getBookingDetailsByUserId(Long userId) {
        logger.info("Fetching booking details for user with ID: {}", userId);
        return bookingRepository.findBookingDetailsByUserId(userId);
    }

}
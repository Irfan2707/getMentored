package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.SlotDTO;
import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTO;
import com.nineleaps.authentication.jwt.entity.Booking;
import com.nineleaps.authentication.jwt.entity.Slot;
import com.nineleaps.authentication.jwt.enums.BookingStatus;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import com.nineleaps.authentication.jwt.repository.BookingRepository;
import com.nineleaps.authentication.jwt.repository.SlotRepository;
import com.nineleaps.authentication.jwt.service.interfaces.ISlotService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;

@Service

public class SlotServiceImpl implements ISlotService {

    @Autowired
    private final SlotRepository slotRepository;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final EmailServiceImpl emailService;


    private static final Logger logger = LoggerFactory.getLogger(SlotServiceImpl.class);

    @Autowired
    public SlotServiceImpl(
            SlotRepository slotRepository,
            ModelMapper modelMapper,
            BookingRepository bookingRepository,
            EmailServiceImpl emailService
    ) {
        this.slotRepository = slotRepository;
        this.modelMapper = modelMapper;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }


    @Override
    public SlotDTO createSlot(SlotDTO slotDTO) throws ConflictException {
        logger.info("Creating a new slot");
        Slot slot = modelMapper.map(slotDTO, Slot.class);
        slot.setCreatedAt(LocalDateTime.now());
        slot.setStatus(BookingStatus.PENDING); // Set the initial status as PENDING

        // Check for existing slot with the same start and end date times and mentor ID
        LocalDateTime newSlotStart = slot.getStartDateTime();
        LocalDateTime newSlotEnd = slot.getEndDateTime();

        if (slotRepository.existsByStartDateTimeAndEndDateTimeAndMentorId(
                newSlotStart, newSlotEnd, slot.getMentorId())) {
            throw new ConflictException("Slot already exists with the same start and end date times and mentor ID");
        }

        // Check for overlapping slots within a one-hour window
        LocalDateTime overlappingSlotStart = newSlotStart.minusHours(1);
        LocalDateTime overlappingSlotEnd = newSlotEnd.plusHours(1);

        List<Slot> overlappingSlots = slotRepository.findByMentorIdAndStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                slot.getMentorId(), overlappingSlotEnd, overlappingSlotStart
        );

        if (!overlappingSlots.isEmpty()) {
            throw new ConflictException("Overlapping slot found within a one-hour window");
        }

        Slot createdSlot = slotRepository.save(slot);
        logger.info("Slot created with ID: {}", createdSlot.getId());
        return modelMapper.map(createdSlot, SlotDTO.class);
    }

    @Override
    public SlotDTO updateSlot(Long id, SlotDTO slotDTO) throws ResourceNotFoundException {
        logger.info("Updating slot with ID: {}", id);
        Slot slot = slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with this id " + id));

        // Update the slot object with the data from the DTO
        slot.setStartDateTime(slotDTO.getStartDateTime());
        slot.setEndDateTime(slotDTO.getEndDateTime());
        slot.setMentorId(slotDTO.getMentorId());
        slot.setCreatedAt(LocalDateTime.now());

        // Save the updated slot object
        Slot updatedSlot = slotRepository.save(slot);
        logger.info("Slot updated with ID: {}", updatedSlot.getId());

        // Map the updated slot to a DTO and return it
        return modelMapper.map(updatedSlot, SlotDTO.class);
    }

    @Transactional
    @Override
    public boolean deleteSlot(Long id) throws ResourceNotFoundException {
        try {
            logger.info("Deleting slot with ID: {}", id);
            Slot slot = slotRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Slot not found with the id " + id));

            // Get the associated bookings for the slot
            List<Booking> bookingsForSlot = bookingRepository.findBySlotId(slot.getId());

            // Send email notifications to the mentees about the slot cancellation
            for (Booking booking : bookingsForSlot) {
                String menteeEmail = booking.getMentee().getUserMail();
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                String bookingDateTimeFormatted = booking.getBookingDateTime().format(formatter);
                String subject = "Slot Cancellation Notification";
                String content = "The slot scheduled for " + bookingDateTimeFormatted + " has been cancelled by the mentor.";
                emailService.sendEmail(subject, content, menteeEmail);
            }

            // Soft delete the slot by marking it as deleted and updating the updatedTime
            slot.setDeleted(true);
            slot.setUpdatedTime(LocalDateTime.now());

            // Save the updated slot with soft delete information
            slotRepository.save(slot);

            // Soft delete the associated bookings
            for (Booking booking : bookingsForSlot) {
                booking.setDeleted(true);
                booking.setUpdatedTime(LocalDateTime.now());
            }

            // Save the updated bookings with soft delete information
            bookingRepository.saveAll(bookingsForSlot);

            logger.info("Slot with ID: {} and associated bookings marked as deleted", id);
        } catch (ResourceNotFoundException e) {

            logger.error("Error marking slot and associated bookings as deleted with ID: {}", id, e);

            throw new ResourceNotFoundException("Slot not found with id " + id);
        }
        return false;
    }


    @Override
    public Page<SlotDTO> getSlotsByMentorId(Long mentorId, int pageNumber, int pageSize) {
        logger.info("Fetching slots for mentor with ID: {}", mentorId);
        LocalDate currentDate = LocalDate.now();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Slot> slotsPage = slotRepository.findByMentorIdAndStartDateTimeAfter(mentorId, currentDate.atStartOfDay(), pageable);

        return slotsPage.map(slot -> modelMapper.map(slot, SlotDTO.class));
    }


    @Override
    public void setStatus(Long slotId, BookingStatus bookingStatus) throws ResourceNotFoundException {
        logger.info("Setting status for slot with ID: {} to {}", slotId, bookingStatus);
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id " + slotId));
        slot.setStatus(bookingStatus);
        slotRepository.save(slot);
    }

    @Transactional
    @Override
    public boolean deleteMultipleSlotsById(List<Long> slotIds) {
        for (Long slotId : slotIds) {
            // Get the slots before deletion for later use in the email
            Optional<Slot> optionalSlot = slotRepository.findById(slotId);
            if (optionalSlot.isPresent()) {
                Slot slot = optionalSlot.get();

                // Fetch the associated bookings to get mentee's information
                List<Booking> bookings = slot.getBookings();
                for (Booking booking : bookings) {
                    sendCancellationEmailToMentee(booking);
                }

                // Soft delete the slot by marking it as deleted and updating the updatedTime
                slot.setDeleted(true);
                slot.setUpdatedTime(LocalDateTime.now());

                // Save the updated slot with soft delete information
                slotRepository.save(slot);

                // Soft delete the associated bookings
                for (Booking booking : bookings) {
                    booking.setDeleted(true);
                    booking.setUpdatedTime(LocalDateTime.now());
                }

                // Save the updated bookings with soft delete information
                bookingRepository.saveAll(bookings);

                logger.info("Slot with ID: {} and associated bookings marked as deleted", slotId);
            }
        }
        return false;
    }

    public void sendCancellationEmailToMentee(Booking booking) {
        String menteeEmail = booking.getMentee().getUserMail();
        String mentorName = booking.getMentor().getUserName();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        String startDateTimeFormatted = booking.getSlot().getStartDateTime().format(formatter);
        String endDateTimeFormatted = booking.getSlot().getEndDateTime().format(formatter);
        String subject = "Booking Cancellation Notification";
        String content = "Your booking with Mentor " + mentorName + " from " + startDateTimeFormatted + " to " + endDateTimeFormatted + " has been canceled.";

        // Assuming you have an EmailService to send the email
        emailService.sendEmail(subject, content, menteeEmail);
    }

    @Override
    public void deleteMultipleSlots(List<Slot> slots) {
        for (var slot : slots) {
            slotRepository.deleteById(slot.getId());
        }
    }

    @Override
    public SlotStatisticsDTO getSlotCountsByMentorAndDateRange(
            Long mentorId, String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atStartOfDay().plusDays(1L);

        long totalSlots = slotRepository.getTotalSlotsByMentorAndDateRange(mentorId, start, end);
        long pendingSlots = slotRepository.getPendingSlotsByMentorAndDateRange(mentorId, start, end);
        long bookedSlots = slotRepository.getBookedSlotsByMentorAndDateRange(mentorId, start, end);

        return new SlotStatisticsDTO(totalSlots, pendingSlots, bookedSlots);
    }
}





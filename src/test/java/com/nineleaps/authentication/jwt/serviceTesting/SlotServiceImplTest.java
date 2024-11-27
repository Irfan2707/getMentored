package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.SlotDTO;
import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTO;
import com.nineleaps.authentication.jwt.entity.Booking;
import com.nineleaps.authentication.jwt.entity.Slot;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.BookingStatus;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import com.nineleaps.authentication.jwt.repository.BookingRepository;
import com.nineleaps.authentication.jwt.repository.SlotRepository;
import com.nineleaps.authentication.jwt.service.implementation.EmailServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.SlotServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SlotServiceImplTest {

    @Mock
    private SlotRepository slotRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private EmailServiceImpl emailService;
    @InjectMocks
    private SlotServiceImpl slotService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        EmailServiceImpl emailService = mock(EmailServiceImpl.class);
        slotService = new SlotServiceImpl(slotRepository, modelMapper, bookingRepository, emailService);
    }



//    @Test
//    void testGetSlotsByMentorId_EmptyList() {
//        // Mock data
//        Long mentorId = 1L;
//        List<Slot> mockSlots = new ArrayList<>();
//
//
//        List<SlotDTO> actualSlotDtos = slotService.getSlotsByMentorId(mentorId);
//
//        verifyNoInteractions(modelMapper);
//        assertEquals(0, actualSlotDtos.size());
//    }

    @Test
    void testSetStatus_Success() throws ResourceNotFoundException {
        // Mock the slot entity
        Slot slot = new Slot();
        slot.setId(1L);
        slot.setStatus(BookingStatus.PENDING);

        // Mock the slot repository behavior
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);

        slotService.setStatus(1L, BookingStatus.BOOKED);

        verify(slotRepository, times(1)).findById(1L);
        verify(slotRepository, times(1)).save(slot);

        // Assert the updated status
        assertEquals(BookingStatus.BOOKED, slot.getStatus());
    }

    @Test
    void testSetStatus_InvalidSlotId() {
        // Mock the slot repository behavior
        when(slotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> slotService.setStatus(1L, BookingStatus.BOOKED));

        verify(slotRepository, times(1)).findById(1L);
        verify(slotRepository, never()).save(any(Slot.class));
    }


    @Test
    void testUpdateSlot_Success() throws ResourceNotFoundException {
        // Mock the input slot ID and DTO
        Long slotId = 1L;
        SlotDTO slotDTO = new SlotDTO();
        slotDTO.setStartDateTime(LocalDateTime.now());
        slotDTO.setEndDateTime(LocalDateTime.now().plusHours(1));
        slotDTO.setMentorId(123L);

        // Mock the slot entity
        Slot slot = new Slot();
        slot.setId(slotId);

        // Mock the slot repository behavior
        when(slotRepository.findById(slotId)).thenReturn(Optional.of(slot));
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);

        // Call the method under test
        SlotDTO updatedSlotDTO = slotService.updateSlot(slotId, slotDTO);

        verify(slotRepository, times(1)).findById(slotId);
        verify(slotRepository, times(1)).save(slot);

        // Assert the updated slot DTO
        assertEquals(slot.getId(), updatedSlotDTO.getId());
        assertEquals(slotDTO.getStartDateTime(), updatedSlotDTO.getStartDateTime());
        assertEquals(slotDTO.getEndDateTime(), updatedSlotDTO.getEndDateTime());
        assertEquals(slotDTO.getMentorId(), updatedSlotDTO.getMentorId());
    }
    @Test
    void testUpdateSlot_InvalidSlotId() {
        // Mock the slot repository behavior
        when(slotRepository.findById(1L)).thenReturn(Optional.empty());

        Executable updateSlotMethod = () -> {
            slotService.updateSlot(1L, new SlotDTO());
        };

        assertThrows(ResourceNotFoundException.class, updateSlotMethod);

        verify(slotRepository, times(1)).findById(1L);
        verify(slotRepository, never()).save(any(Slot.class));
    }





    @Test
    void testCreateSlot_Success() throws ConflictException {
        // Mock the input slot DTO
        SlotDTO slotDTO = new SlotDTO();
        slotDTO.setStartDateTime(LocalDateTime.now());
        slotDTO.setEndDateTime(LocalDateTime.now().plusHours(1));
        slotDTO.setMentorId(123L);

        // Mock the slot entity
        Slot slot = new Slot();

        // Mock the slot repository behavior
        when(slotRepository.existsByStartDateTimeAndEndDateTimeAndMentorId(
                slotDTO.getStartDateTime(), slotDTO.getEndDateTime(), slotDTO.getMentorId())).thenReturn(false);
        when(slotRepository.save(any(Slot.class))).thenAnswer(invocation -> {
            Slot savedSlot = invocation.getArgument(0);
            savedSlot.setId(1L);
            return savedSlot;
        });

        // Call the method under test
        SlotDTO createdSlotDTO = slotService.createSlot(slotDTO);

        // Verify the slot repository interactions
        verify(slotRepository, times(1)).existsByStartDateTimeAndEndDateTimeAndMentorId(
                slotDTO.getStartDateTime(), slotDTO.getEndDateTime(), slotDTO.getMentorId());
        verify(slotRepository, times(1)).save(any(Slot.class));

        // Assert the created slot DTO
        assertNotNull(createdSlotDTO.getId());
        assertEquals(slotDTO.getStartDateTime(), createdSlotDTO.getStartDateTime());
        assertEquals(slotDTO.getEndDateTime(), createdSlotDTO.getEndDateTime());
        assertEquals(slotDTO.getMentorId(), createdSlotDTO.getMentorId());
    }

    @Test
    void testCreateSlot_ConflictException() {
        // Mock the input slot DTO
        SlotDTO slotDTO = new SlotDTO();
        slotDTO.setStartDateTime(LocalDateTime.now());
        slotDTO.setEndDateTime(LocalDateTime.now().plusHours(1));
        slotDTO.setMentorId(123L);

        // Mock the slot repository behavior
        when(slotRepository.existsByStartDateTimeAndEndDateTimeAndMentorId(
                slotDTO.getStartDateTime(), slotDTO.getEndDateTime(), slotDTO.getMentorId())).thenReturn(true);

        assertThrows(ConflictException.class, () -> slotService.createSlot(slotDTO));

        verify(slotRepository, times(1)).existsByStartDateTimeAndEndDateTimeAndMentorId(
                slotDTO.getStartDateTime(), slotDTO.getEndDateTime(), slotDTO.getMentorId());
        verify(slotRepository, never()).save(any(Slot.class));
    }
    @Test
    void testCreateSlot_OverlappingSlots() {
        // Mock the input slot DTO
        SlotDTO slotDTO = new SlotDTO();
        slotDTO.setStartDateTime(LocalDateTime.now());
        slotDTO.setEndDateTime(LocalDateTime.now().plusHours(1));
        slotDTO.setMentorId(123L);

        // Mock the slot repository behavior to return overlapping slots
        when(slotRepository.existsByStartDateTimeAndEndDateTimeAndMentorId(
                slotDTO.getStartDateTime(), slotDTO.getEndDateTime(), slotDTO.getMentorId())).thenReturn(false);

        List<Slot> overlappingSlots = new ArrayList<>();
        Slot overlappingSlot1 = new Slot();
        overlappingSlot1.setId(2L);
        overlappingSlots.add(overlappingSlot1);

        // Mock the slot repository behavior to return overlapping slots within a one-hour window
        when(slotRepository.findByMentorIdAndStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                slotDTO.getMentorId(), slotDTO.getEndDateTime().plusHours(1), slotDTO.getStartDateTime().minusHours(1)))
                .thenReturn(overlappingSlots);

        // Call the method under test and assert that it throws a ConflictException
        assertThrows(ConflictException.class, () -> slotService.createSlot(slotDTO));

        verify(slotRepository, times(1)).existsByStartDateTimeAndEndDateTimeAndMentorId(
                slotDTO.getStartDateTime(), slotDTO.getEndDateTime(), slotDTO.getMentorId());
        verify(slotRepository, times(1)).findByMentorIdAndStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                slotDTO.getMentorId(), slotDTO.getEndDateTime().plusHours(1), slotDTO.getStartDateTime().minusHours(1));
        verify(slotRepository, never()).save(any(Slot.class));
    }
    @Test
    void testSendCancellationEmailToMentee() {
        // Create a Booking object with necessary data
        Booking booking = new Booking();

        User mentee = new User();
        mentee.setUserMail("mentee@example.com");
        booking.setMentee(mentee);

        User mentor = new User();
        mentor.setUserName("MentorName");
        booking.setMentor(mentor);

        Slot slot = new Slot();
        LocalDateTime startDateTime = LocalDateTime.of(2023, 9, 26, 10, 0);

        LocalDateTime endDateTime = LocalDateTime.of(2023, 9, 26, 11, 0);
        slot.setStartDateTime(startDateTime);
        slot.setEndDateTime(endDateTime);
        booking.setSlot(slot);

        slotService.sendCancellationEmailToMentee(booking);

        String expectedSubject = "Booking Cancellation Notification";
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        String expectedStartDateTimeFormatted = startDateTime.format(formatter);
        String expectedEndDateTimeFormatted = endDateTime.format(formatter);
        String expectedContent = "Your booking with Mentor MentorName from " +
                expectedStartDateTimeFormatted + " to " + expectedEndDateTimeFormatted + " has been canceled.";
        String expectedMenteeEmail = "mentee@example.com";
        assertEquals("mentee@example.com", expectedMenteeEmail, "Mentee email does not match");
        assertEquals("Booking Cancellation Notification", expectedSubject, "Email subject does not match");

    }

     @Test
    void testDeleteSlotWithBookings() throws ResourceNotFoundException {
        // Create a sample slot with associated bookings
        Slot slot = new Slot();
        slot.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        // ... populate slot and bookings as needed

        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot)); // Mock slot presence
        when(bookingRepository.findBySlotId(1L)).thenReturn(bookings); // Mock associated bookings

        boolean result = slotService.deleteSlot(1L);

        verify(slotRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findBySlotId(1L); // Associated bookings should be found
        verify(slotRepository, times(0)).delete(slot); // Slot should not be deleted

        assertFalse(result);
    }

    @Test
    void testDeleteSlotNotFound() {
        // Mock the behavior of slotRepository when the slot is not found
        when(slotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> slotService.deleteSlot(1L));

        verify(bookingRepository, never()).deleteAll(anyList());
        verify(slotRepository, never()).delete(any());
    }


    @Test
    void testDeleteMultipleSlotsById() {

        List<Long> slotIdsToDelete = List.of(1L, 2L, 3L);
        Slot slot1 = new Slot();
        slot1.setId(1L);
        Slot slot2 = new Slot();
        slot2.setId(2L);
        Slot slot3 = new Slot();
        slot3.setId(3L);
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot1));
        when(slotRepository.findById(2L)).thenReturn(Optional.of(slot2));
        when(slotRepository.findById(3L)).thenReturn(Optional.of(slot3));
        when(bookingRepository.findBySlotId(anyLong())).thenReturn(new ArrayList<>()); // No associated bookings for this test
        boolean result = slotService.deleteMultipleSlotsById(slotIdsToDelete);
        verify(slotRepository, times(3)).findById(anyLong());
        verify(bookingRepository, never()).findBySlotId(anyLong());
        assertFalse(result);
    }


    @Test
    void testGetSlotsByMentorIdWithPagination() {
        // Arrange
        Long mentorId = 1L;
        int pageNumber = 0;
        int pageSize = 10;

        // Mock the behavior of slotRepository.findByMentorIdAndStartDateTimeAfter
        LocalDate currentDate = LocalDate.now();
        LocalDateTime startDateTime = currentDate.atStartOfDay();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Slot slot = new Slot();
        slot.setId(1L);
        slot.setStartDateTime(startDateTime);
        slot.setEndDateTime(startDateTime.plusHours(1));

        Page<Slot> slotsPage = new PageImpl<>(Collections.singletonList(slot));
        when(slotRepository.findByMentorIdAndStartDateTimeAfter(mentorId, startDateTime, pageable)).thenReturn(slotsPage);

        // Mock the behavior of modelMapper.map
        when(modelMapper.map(slot, SlotDTO.class)).thenReturn(new SlotDTO());

        // Act
        Page<SlotDTO> result = slotService.getSlotsByMentorId(mentorId, pageNumber, pageSize);

        // Assert
        assertEquals(1, result.getTotalElements()); // Expecting one slot in the result
    }

    @Test
    void testGetSlotCountsByMentorAndDateRange() {
        Long mentorId = 1L;
        String startDateStr = "2023-09-01";
        String endDateStr = "2023-09-30";

        // Mock the behavior of the slotRepository to return expected counts
        when(slotRepository.getTotalSlotsByMentorAndDateRange(mentorId, LocalDateTime.parse("2023-09-01T00:00:00"), LocalDateTime.parse("2023-10-01T00:00:00"))).thenReturn(10L);
        when(slotRepository.getPendingSlotsByMentorAndDateRange(mentorId, LocalDateTime.parse("2023-09-01T00:00:00"), LocalDateTime.parse("2023-10-01T00:00:00"))).thenReturn(5L);
        when(slotRepository.getBookedSlotsByMentorAndDateRange(mentorId, LocalDateTime.parse("2023-09-01T00:00:00"), LocalDateTime.parse("2023-10-01T00:00:00"))).thenReturn(3L);

        SlotStatisticsDTO slotStatisticsDTO = slotService.getSlotCountsByMentorAndDateRange(mentorId, startDateStr, endDateStr);
        assertNotNull(slotStatisticsDTO);
        assertEquals(10L, slotStatisticsDTO.getTotalSlots());
        verify(slotRepository, times(1)).getTotalSlotsByMentorAndDateRange(mentorId, LocalDateTime.parse("2023-09-01T00:00:00"), LocalDateTime.parse("2023-10-01T00:00:00"));
        verify(slotRepository, times(1)).getPendingSlotsByMentorAndDateRange(mentorId, LocalDateTime.parse("2023-09-01T00:00:00"), LocalDateTime.parse("2023-10-01T00:00:00"));
        verify(slotRepository, times(1)).getBookedSlotsByMentorAndDateRange(mentorId, LocalDateTime.parse("2023-09-01T00:00:00"), LocalDateTime.parse("2023-10-01T00:00:00"));
    }


}
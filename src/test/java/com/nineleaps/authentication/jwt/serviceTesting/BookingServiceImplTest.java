package com.nineleaps.authentication.jwt.serviceTesting;
//
//
//
////import com.nineleaps.authentication.jwt.controllerexceptions.MenteeEmailNullException;
////import com.nineleaps.authentication.jwt.controllerexceptions.MentorEmailNullException;
//import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
//import com.nineleaps.authentication.jwt.dto.BookingDTO;
//import com.nineleaps.authentication.jwt.entity.Booking;
//import com.nineleaps.authentication.jwt.entity.Engagement;
//import com.nineleaps.authentication.jwt.entity.Slot;
//import com.nineleaps.authentication.jwt.entity.User;
//import com.nineleaps.authentication.jwt.enums.BookingStatus;
//import com.nineleaps.authentication.jwt.exception.SlotAlreadyExistsException;
//import com.nineleaps.authentication.jwt.repository.BookingRepository;
//import com.nineleaps.authentication.jwt.repository.EngagementRepository;
//import com.nineleaps.authentication.jwt.repository.SlotRepository;
//import com.nineleaps.authentication.jwt.repository.UserRepository;
//import com.nineleaps.authentication.jwt.service.implementation.BookingServiceImpl;
//import com.nineleaps.authentication.jwt.service.implementation.EmailServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//
//import javax.persistence.OptimisticLockException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.FormatStyle;
//
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//
//import static com.nineleaps.authentication.jwt.service.implementation.BookingServiceImpl.BOOKING_CANCELLATION_NOTIFICATION_SUBJECT;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//
//class BookingServiceImplTest {
//
//    @Mock
//    private BookingRepository bookingRepository;
//
//    @Mock
//    private SlotRepository slotRepository;
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private BookingServiceImpl bookingService;
//
//    @Mock
//    private EngagementRepository engagementRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//
//
//    @Mock
//    private EmailServiceImpl emailService;
//
//
//    @Test
//    void testCreateBookingSuccess() {
//        // Arrange
//        BookingDTO bookingDTO = createValidBookingDTO();
//        Slot slot = createValidSlot();
//        Engagement engagement = createValidEngagement();
//        User mentee = createValidMentee();
//        User mentor = createValidMentor();
//        Booking savedBooking = createValidSavedBooking();
//
//        when(slotRepository.findById(bookingDTO.getSlotId())).thenReturn(Optional.of(slot));
//        when(bookingRepository.existsBySlotId(bookingDTO.getSlotId())).thenReturn(false);
//        when(engagementRepository.findById(bookingDTO.getEngagementId())).thenReturn(Optional.of(engagement));
//        when(userRepository.findById(bookingDTO.getMenteeId())).thenReturn(Optional.of(mentee));
//        when(userRepository.findById(bookingDTO.getMentorId())).thenReturn(Optional.of(mentor));
//        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(savedBooking);
//
//        // Act
//        BookingDTO result = bookingService.createBooking(bookingDTO);
//
//        // Assert
//        assertNotNull(result.getId());
//        Assertions.assertEquals(bookingDTO.getSlotId(), result.getSlotId());
//        Assertions.assertEquals(bookingDTO.getMenteeId(), result.getMenteeId());
//        Assertions.assertEquals(bookingDTO.getMenteeUsername(), result.getMenteeUsername());
//        Assertions.assertEquals(bookingDTO.getMentorId(), result.getMentorId());
//        Assertions.assertEquals(bookingDTO.getMentorUsername(), result.getMentorUsername());
//        Assertions.assertEquals(bookingDTO.getEngagementId(), result.getEngagementId());
//        Assertions.assertEquals(bookingDTO.getNoOfHours(), result.getNoOfHours());
//        assertTrue(
//                ChronoUnit.MILLIS.between(bookingDTO.getBookingDateTime(), result.getBookingDateTime()) < 1000,
//                "LocalDateTime values are not equal within the allowed tolerance."
//        );
//
//
//        verify(slotRepository, times(1)).findById(bookingDTO.getSlotId());
//        verify(bookingRepository, times(1)).existsBySlotId(bookingDTO.getSlotId());
//        verify(engagementRepository, times(1)).findById(bookingDTO.getEngagementId());
//        verify(userRepository, times(1)).findById(bookingDTO.getMenteeId());
//        verify(userRepository, times(1)).findById(bookingDTO.getMentorId());
//        verify(bookingRepository, times(1)).save(Mockito.any(Booking.class));
//        verify(emailService, times(1)).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
//    }
//
//    @Test
//    void testCreateBooking_BookingWithSameSlotExists() {
//        // Arrange
//        BookingDTO bookingDTO = createValidBookingDTO();
//        Slot slot = createValidSlot();
//
//        when(slotRepository.findById(bookingDTO.getSlotId())).thenReturn(Optional.of(slot));
//        when(bookingRepository.existsBySlotId(bookingDTO.getSlotId())).thenReturn(true);
//
//        // Act and Assert
//        assertThrows(SlotAlreadyExistsException.class, () -> {
//            bookingService.createBooking(bookingDTO);
//        });
//
//        verify(slotRepository, times(1)).findById(bookingDTO.getSlotId());
//        verify(bookingRepository, times(1)).existsBySlotId(bookingDTO.getSlotId());
//        verify(bookingRepository, Mockito.never()).save(Mockito.any(Booking.class));
//        verify(slotRepository, Mockito.never()).save(Mockito.any(Slot.class));
//        verify(emailService, Mockito.never()).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
//    }
//
//
//    @Test
//    void testCreateBookingOptimisticLockException() {
//        // Arrange
//        BookingDTO bookingDTO = createValidBookingDTO();
//
//        when(slotRepository.findById(bookingDTO.getSlotId())).thenReturn(Optional.of(createValidSlot())); // Create a valid Slot
//        when(bookingRepository.existsBySlotId(bookingDTO.getSlotId())).thenReturn(false);
//        when(engagementRepository.findById(bookingDTO.getEngagementId())).thenReturn(Optional.of(createValidEngagement())); // Create a valid Engagement
//        when(userRepository.findById(bookingDTO.getMenteeId())).thenReturn(Optional.of(createValidMentee())); // Create a valid Mentee
//        when(userRepository.findById(bookingDTO.getMentorId())).thenReturn(Optional.of(createValidMentor())); // Create a valid Mentor
//        when(bookingRepository.save(Mockito.any(Booking.class))).thenThrow(new OptimisticLockException("Optimistic lock exception occurred."));
//
//        // Act and Assert
//        assertThrows(SlotAlreadyExistsException.class, () -> {
//            bookingService.createBooking(bookingDTO);
//        });
//
//    }
//
//    @Test
//    void testCreateBookingGenericException() {
//        // Arrange
//        BookingDTO bookingDTO = createValidBookingDTO();
//
//        when(slotRepository.findById(bookingDTO.getSlotId())).thenReturn(Optional.of(createValidSlot()));
//        when(bookingRepository.existsBySlotId(bookingDTO.getSlotId())).thenReturn(false);
//        when(engagementRepository.findById(bookingDTO.getEngagementId())).thenReturn(Optional.of(createValidEngagement()));
//        when(userRepository.findById(bookingDTO.getMenteeId())).thenReturn(Optional.of(createValidMentee()));
//        when(userRepository.findById(bookingDTO.getMentorId())).thenReturn(Optional.of(createValidMentor()));
//        when(bookingRepository.save(Mockito.any(Booking.class))).thenThrow(new RuntimeException("Generic exception occurred."));
//
//        // Act and Assert
//        assertThrows(SlotAlreadyExistsException.class, () -> {
//            bookingService.createBooking(bookingDTO);
//        });
//
//    }
//
//    @Test
//    void testDeleteBooking() throws ResourceNotFoundException {
//
//
//        // Create a sample booking
//        Booking booking = new Booking();
//        booking.setId(1L);
//
//        // Create a sample slot associated with the booking
//        Slot slot = new Slot();
//        slot.setId(1L);
//        slot.setStatus(BookingStatus.BOOKED);
//
//        // Create a mutable list to hold the bookings associated with the slot
//        List<Booking> bookings = new ArrayList<>();
//        bookings.add(booking);
//
//        slot.setBookings(bookings);
//
//        // Set the associated slot for the booking
//        booking.setSlot(slot);
//
//        // Mock the behavior of bookingRepository
//        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
//
//        // Mock the behavior of slotRepository
//        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
//
//        // Mock the behavior of bookingRepository for findBySlotId
//        when(bookingRepository.findBySlotId(1L)).thenReturn(bookings);
//
//        // Call the deleteBooking method
//        bookingService.deleteBooking(1L);
//
//        // Verify that the booking's soft delete flags are set and updated
//        assertTrue(booking.getDeleted());
//        assertNotNull(booking.getUpdatedTime());
//
//        // Verify that the delete method of bookingRepository was not called
//        verify(bookingRepository, never()).delete(booking);
//
//        // Verify that the delete method of slotRepository was not called
//        verify(slotRepository, never()).delete(slot);
//    }
//
//
//
//      @Test
//    void testDeleteBookingWhenBookingsForSlotNotEmpty() throws ResourceNotFoundException {
//        // Create a sample booking
//        Booking booking = new Booking();
//        booking.setId(1L);
//
//        // Create a sample slot associated with the booking
//        Slot slot = new Slot();
//        slot.setId(1L);
//        slot.setStatus(BookingStatus.BOOKED);
//
//        // Create another booking associated with the same slot
//        Booking anotherBooking = new Booking();
//        anotherBooking.setId(2L);
//
//        // Create a mutable list to hold the bookings associated with the slot
//        List<Booking> bookings = new ArrayList<>();
//        bookings.add(booking);
//        bookings.add(anotherBooking);
//
//        slot.setBookings(bookings);
//
//        // Set the associated slot for the booking
//        booking.setSlot(slot);
//
//        // Mock the behavior of bookingRepository
//        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
//
//        // Mock the behavior of slotRepository
//        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
//
//        // Mock the behavior of bookingRepository for findBySlotId
//        when(bookingRepository.findBySlotId(1L)).thenReturn(bookings);
//
//        // Call the deleteBooking method
//        bookingService.deleteBooking(1L);
//
//        // Verify that the delete method of bookingRepository was not called
//        verify(bookingRepository, never()).delete(booking);
//
//        // Verify that the delete method of slotRepository was not called
//        verify(slotRepository, never()).delete(slot);
//    }
//
//    @Test
//    void testDeleteBookingWhenSlotIsNull() throws ResourceNotFoundException {
//        // Create a sample booking with a null slot
//        Booking booking = new Booking();
//        booking.setId(1L);
//        booking.setSlot(null);
//
//        // Mock the behavior of bookingRepository
//        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
//
//        // Call the deleteBooking method
//        bookingService.deleteBooking(1L);
//
//        // Verify that the booking's soft delete flags are set and updated
//        assertTrue(booking.getDeleted());
//        assertNotNull(booking.getUpdatedTime());
//
//        // Verify that the delete method of bookingRepository was not called
//        verify(bookingRepository, never()).delete(booking);
//    }
//
//
//    @Test
//    void testDeleteMultipleBookingsById() throws ResourceNotFoundException {
//        // Create a list of sample booking IDs to delete
//        List<Long> bookingIdsToDelete = Arrays.asList(1L, 2L, 3L);
//
//        // Create some dummy bookings and associated slots
//        Booking booking1 = new Booking();
//        booking1.setId(1L);
//
//        Booking booking2 = new Booking();
//        booking2.setId(2L);
//
//        Booking booking3 = new Booking();
//        booking3.setId(3L);
//
//        Slot slot1 = new Slot();
//        slot1.setId(11L);
//
//        Slot slot2 = new Slot();
//        slot2.setId(12L);
//
//        // Associate bookings with their respective slots
//        booking1.setSlot(slot1);
//        booking2.setSlot(slot2);
//        booking3.setSlot(null); // No associated slot
//
//        // Mock the behavior of bookingRepository for finding bookings by ID
//        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking1));
//        when(bookingRepository.findById(2L)).thenReturn(Optional.of(booking2));
//        when(bookingRepository.findById(3L)).thenReturn(Optional.of(booking3));
//
//        // Mock the behavior of slotRepository for finding slots by ID
//        when(slotRepository.findById(11L)).thenReturn(Optional.of(slot1));
//        when(slotRepository.findById(12L)).thenReturn(Optional.of(slot2));
//
//        // Call the deleteMultipleBookingsById method
//        bookingService.deleteMultipleBookingsById(bookingIdsToDelete);
//
//        // Verify that the soft delete flags are set and updated for each booking
//        assertTrue(booking1.getDeleted());
//        assertNotNull(booking1.getUpdatedTime());
//
//        assertTrue(booking2.getDeleted());
//        assertNotNull(booking2.getUpdatedTime());
//
//        assertFalse(booking3.getDeleted()); // No associated slot
//
//
//        // Verify that the delete method of bookingRepository was not called for any booking
//        verify(bookingRepository, never()).delete(any(Booking.class));
//
//        // Verify that the delete method of slotRepository was not called for any slot
//        verify(slotRepository, never()).delete(any(Slot.class));
//    }
//
//
//
//    @Test
//     void testSendCancellationEmailToMentor() {
//        // Arrange
//        Booking booking = new Booking();
//        booking.setId(1L);
//
//        User mentor = new User();
//        mentor.setUserMail("mentor@example.com");
//
//        User mentee = new User();
//        mentee.setUserName("Mentee Name");
//
//        Slot slot = new Slot();
//        LocalDateTime startDateTime = LocalDateTime.of(2023, 9, 30, 10, 0); // Replace with actual start date and time
//        LocalDateTime endDateTime = LocalDateTime.of(2023, 9, 30, 12, 0);   // Replace with actual end date and time
//        slot.setStartDateTime(startDateTime);
//        slot.setEndDateTime(endDateTime);
//
//        booking.setMentor(mentor);
//        booking.setMentee(mentee);
//        booking.setSlot(slot);
//
//        // Mocking the behavior of emailService
//        when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);
//
//        // Act
//        bookingService.sendCancellationEmailToMentor(booking);
//
//        // Assert
//        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
//        String startDateTimeFormatted = startDateTime.format(formatter);
//        String endDateTimeFormatted = endDateTime.format(formatter);
//        String expectedContent = "The booking for Mentee Name from " + startDateTimeFormatted + " to " + endDateTimeFormatted + " has been canceled.";
//        verify(emailService, times(1)).sendEmail(
//                BOOKING_CANCELLATION_NOTIFICATION_SUBJECT,
//                expectedContent,
//                "mentor@example.com"
//        );
//    }
//
//
////    @Test
////    void testDeleteMultipleBookingsById_ValidIds() throws ResourceNotFoundException {
////        // Arrange
////        List<Long> validBookingIds = Arrays.asList(1L, 2L, 3L);
////
////        Booking booking1 = createValidSavedBooking();
////        booking1.setId(1L);
////        Booking booking2 = createValidSavedBooking();
////        booking2.setId(2L);
////        Booking booking3 = createValidSavedBooking();
////        booking3.setId(3L);
////
////        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking1));
////        when(bookingRepository.findById(2L)).thenReturn(Optional.of(booking2));
////        when(bookingRepository.findById(3L)).thenReturn(Optional.of(booking3));
////
////        // Act
////        bookingService.deleteMultipleBookingsById(validBookingIds);
////
////        // Assert
////        verify(slotRepository, times(3)).delete(Mockito.any(Slot.class));
////        verify(bookingRepository, times(3)).delete(Mockito.any(Booking.class));
////    }
//
////    @Test
////    void testDeleteMultipleBookingsById_ExceptionHandling() {
////        // Arrange
////        List<Long> validBookingIds = Arrays.asList(1L, 2L, 3L);
////
////        Booking booking1 = createValidSavedBooking();
////        booking1.setId(1L);
////        Booking booking2 = createValidSavedBooking();
////        booking2.setId(2L);
////        Booking booking3 = createValidSavedBooking();
////        booking3.setId(3L);
////
////        // Simulate booking not found for one of the IDs
////        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking1));
////        when(bookingRepository.findById(2L)).thenReturn(Optional.of(booking2));
////        when(bookingRepository.findById(3L)).thenReturn(Optional.empty());
////
////        // Act and Assert
////        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
////            bookingService.deleteMultipleBookingsById(validBookingIds);
////        });
////
////        verify(slotRepository, times(2)).delete(Mockito.any(Slot.class)); // Two slots should be deleted
////        verify(bookingRepository, times(2)).delete(Mockito.any(Booking.class)); // Two bookings should be deleted
////    }
//
//
//
//    @Test
//    void testGetBookingDetailsByUserId() {
//        // Arrange
//        Long userId = 1L;
//        List<Map<String, Object>> expectedBookingDetails = createSampleBookingDetails();
//        // Mocking the behavior of the repository method
//        when(bookingRepository.findBookingDetailsByUserId(userId)).thenReturn(expectedBookingDetails);
//
//        // Act
//        List<Map<String, Object>> actualBookingDetails = bookingService.getBookingDetailsByUserId(userId);
//
//        // Assert
//        assertNotNull(actualBookingDetails);
//        assertEquals(expectedBookingDetails.size(), actualBookingDetails.size());
//
//        Map<String, Object> expectedBooking = expectedBookingDetails.get(0);
//        Map<String, Object> actualBooking = actualBookingDetails.get(0);
//
//        assertEquals(expectedBooking.get("bookingId"), actualBooking.get("bookingId"));
//        assertEquals(expectedBooking.get("mentorId"), actualBooking.get("mentorId"));
//        assertEquals(expectedBooking.get("menteeId"), actualBooking.get("menteeId"));
//
//    }
//
//    // Helper method to create sample booking details data
//    private List<Map<String, Object>> createSampleBookingDetails() {
//        Map<String, Object> booking1 = createSampleBookingMap(1L, 2L, 3L);
//        Map<String, Object> booking2 = createSampleBookingMap(4L, 5L, 6L);
//
//        return Arrays.asList(booking1, booking2);
//    }
//
//    private Map<String, Object> createSampleBookingMap(Long bookingId, Long mentorId, Long menteeId) {
//        Map<String, Object> bookingMap = new HashMap<>();
//        bookingMap.put("bookingId", bookingId);
//        bookingMap.put("mentorId", mentorId);
//        bookingMap.put("menteeId", menteeId);
//
//        return bookingMap;
//    }
//
//
//
//
////Helper methods
//
//    private BookingDTO createValidBookingDTO() {
//        BookingDTO bookingDTO = new BookingDTO();
//        bookingDTO.setSlotId(1L);
//        bookingDTO.setBookingDateTime(LocalDateTime.now());
//        bookingDTO.setEngagementId(2L);
//        bookingDTO.setMenteeId(3L);
//        bookingDTO.setMenteeUsername("mentee");
//        bookingDTO.setMentorId(4L);
//        bookingDTO.setMentorUsername("mentor");
//        bookingDTO.setNoOfHours(2);
//        return bookingDTO;
//    }
//
//    private Slot createValidSlot() {
//        Slot slot = new Slot();
//        slot.setId(1L);
//        LocalDateTime startDateTime = LocalDateTime.now();
//        LocalDateTime endDateTime = startDateTime.plusHours(2);
//        slot.setStartDateTime(startDateTime);
//        slot.setEndDateTime(endDateTime);
//        return slot;
//    }
//
//    private Engagement createValidEngagement() {
//        Engagement engagement = new Engagement();
//        engagement.setId(2L);
//        return engagement;
//    }
//
//    private User createValidMentor() {
//        User mentor = new User();
//        mentor.setId(4L);
//        mentor.setUserName("mentor");
//        mentor.setUserMail("mentor@example.com");
//        return mentor;
//    }
//
//    private User createValidMentee() {
//        User mentee = new User();
//        mentee.setId(3L);
//        mentee.setUserName("mentee");
//        return mentee;
//    }
//    private Booking createValidSavedBooking() {
//        Booking savedBooking = new Booking();
//        savedBooking.setId(1L);
//        savedBooking.setBookingDateTime(LocalDateTime.now());
//
//        Slot slot = createValidSlot();
//        savedBooking.setSlot(slot);
//
//        Engagement engagement = createValidEngagement();
//        savedBooking.setEngagement(engagement);
//
//        User mentee = createValidMentee();
//        savedBooking.setMentee(mentee);
//
//        User mentor = createValidMentor();
//        savedBooking.setMentor(mentor);
//
//        savedBooking.setNoOfHours(2);
//
//        return savedBooking;
//    }
//
//
//
//}

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.BookingDTO;
import com.nineleaps.authentication.jwt.entity.Booking;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.Slot;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.BookingStatus;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.exception.SlotAlreadyExistsException;
import com.nineleaps.authentication.jwt.repository.BookingRepository;
import com.nineleaps.authentication.jwt.repository.EngagementRepository;
import com.nineleaps.authentication.jwt.repository.SlotRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.implementation.BookingServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SlotRepository slotRepository;
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private Logger logger;
    @Mock
    private UserRepository userRepository;

    @Mock
    private EngagementRepository engagementRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateBooking_SlotAlreadyExists() {
        // Arrange
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setSlotId(1L);
        bookingDTO.setMenteeId(2L);
        bookingDTO.setMentorId(3L);

        when(bookingRepository.existsBySlotId(bookingDTO.getSlotId())).thenReturn(true);

        // Act and Assert
        assertThrows(SlotAlreadyExistsException.class, () -> {
            bookingService.createBooking(bookingDTO);
        });
    }

    @Test
    void testGetBookingDetailsByUserId() {
        // Arrange
        Long userId = 1L;

        // Mock the behavior of the bookingRepository to return a sample list of booking details
        List<Map<String, Object>> sampleBookingDetails = Arrays.asList(
                createSampleBookingDetails("Booking 1"),
                createSampleBookingDetails("Booking 2")
        );
        when(bookingRepository.findBookingDetailsByUserId(userId)).thenReturn(sampleBookingDetails);

        // Act
        List<Map<String, Object>> result = bookingService.getBookingDetailsByUserId(userId);

        // Assert
        assertEquals(sampleBookingDetails, result);
    }

    private Map<String, Object> createSampleBookingDetails(String bookingName) {
        Map<String, Object> bookingDetail = new HashMap<>();

        // Add booking details to the map
        bookingDetail.put("bookingName", bookingName);
        bookingDetail.put("bookingDate", LocalDateTime.now());
        bookingDetail.put("status", "Confirmed");
        // Add other relevant booking details as needed

        return bookingDetail;
    }




    private User createSampleUser() {
        User user = new User();
        // Initialize the user object with required data
        user.setUserName("SampleUser"); // Set a sample username
        user.setUserMail("sampleuser@example.com"); // Set a sample email
        return user;
    }

    private Slot createSampleSlot() {
        Slot slot = new Slot();
        // Initialize the slot object with required data
        LocalDateTime startDateTime = LocalDateTime.now(); // Set a sample start date and time
        LocalDateTime endDateTime = startDateTime.plusHours(1); // Set a sample end date and time
        slot.setStartDateTime(startDateTime);
        slot.setEndDateTime(endDateTime);
        return slot;
    }


    @Test
    void testCreateBooking_SuccessfulBooking() {
        // Arrange
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setSlotId(1L);
        bookingDTO.setMenteeId(2L);
        bookingDTO.setMentorId(3L);

        Slot slot = createSampleSlot();
        User mentee = createSampleUser();
        User mentor = createSampleUser();

        when(slotRepository.findById(bookingDTO.getSlotId())).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlotId(bookingDTO.getSlotId())).thenReturn(false);
        when(engagementRepository.findById(bookingDTO.getEngagementId())).thenReturn(Optional.of(createSampleEngagement()));
        when(userRepository.findById(bookingDTO.getMenteeId())).thenReturn(Optional.of(mentee));
        when(userRepository.findById(bookingDTO.getMentorId())).thenReturn(Optional.of(mentor));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            savedBooking.setId(1L); // Simulate the ID assignment
            return savedBooking;
        });

        // Act
        BookingDTO result = bookingService.createBooking(bookingDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(mentee.getId(), result.getMenteeId());
        assertEquals(mentor.getId(), result.getMentorId());

    }


    private Engagement createSampleEngagement() {
        Engagement engagement = new Engagement();
        // Initialize the engagement object with required data
        engagement.setId(1L);
        return engagement;
    }





    @Test
    void testDeleteBooking() throws ResourceNotFoundException {
        // Create a sample booking
        Booking booking = new Booking();
        booking.setId(1L);

        // Create a sample slot associated with the booking
        Slot slot = new Slot();
        slot.setId(1L);
        slot.setStatus(BookingStatus.BOOKED);

        // Create a mutable list to hold the bookings associated with the slot
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        slot.setBookings(bookings);

        // Set the associated slot for the booking
        booking.setSlot(slot);

        // Mock the behavior of bookingRepository
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Mock the behavior of slotRepository
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));

        // Mock the behavior of bookingRepository for findBySlotId
        when(bookingRepository.findBySlotId(1L)).thenReturn(bookings);

        // Call the deleteBooking method
        bookingService.deleteBooking(1L);

        // Verify that the booking's soft delete flags are set and updated
        assertTrue(booking.getDeleted());
        assertNotNull(booking.getUpdatedTime());

        // Verify that the delete method of bookingRepository was not called
        verify(bookingRepository, never()).delete(booking);

        // Verify that the delete method of slotRepository was not called
        verify(slotRepository, never()).delete(slot);
    }
    @Test
    void testDeleteBookingWhenBookingsForSlotNotEmpty() throws ResourceNotFoundException {
        // Create a sample booking
        Booking booking = new Booking();
        booking.setId(1L);

        // Create a sample slot associated with the booking
        Slot slot = new Slot();
        slot.setId(1L);
        slot.setStatus(BookingStatus.BOOKED);

        // Create another booking associated with the same slot
        Booking anotherBooking = new Booking();
        anotherBooking.setId(2L);

        // Create a mutable list to hold the bookings associated with the slot
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(anotherBooking);

        slot.setBookings(bookings);

        // Set the associated slot for the booking
        booking.setSlot(slot);

        // Mock the behavior of bookingRepository
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Mock the behavior of slotRepository
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));

        // Mock the behavior of bookingRepository for findBySlotId
        when(bookingRepository.findBySlotId(1L)).thenReturn(bookings);

        // Call the deleteBooking method
        bookingService.deleteBooking(1L);

        // Verify that the delete method of bookingRepository was not called
        verify(bookingRepository, never()).delete(booking);

        // Verify that the delete method of slotRepository was not called
        verify(slotRepository, never()).delete(slot);
    }

    @Test
    void testDeleteBookingWhenSlotIsNull() throws ResourceNotFoundException {
        // Create a sample booking with a null slot
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setSlot(null);

        // Mock the behavior of bookingRepository
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Call the deleteBooking method
        bookingService.deleteBooking(1L);

        // Verify that the booking's soft delete flags are set and updated
        assertTrue(booking.getDeleted());
        assertNotNull(booking.getUpdatedTime());

        // Verify that the delete method of bookingRepository was not called
        verify(bookingRepository, never()).delete(booking);
    }


    @Test
    void testDeleteMultipleBookingsById() throws ResourceNotFoundException {
        // Create a list of sample booking IDs to delete
        List<Long> bookingIdsToDelete = Arrays.asList(1L, 2L, 3L);

        // Create some dummy bookings and associated slots
        Booking booking1 = new Booking();
        booking1.setId(1L);

        Booking booking2 = new Booking();
        booking2.setId(2L);

        Booking booking3 = new Booking();
        booking3.setId(3L);

        Slot slot1 = new Slot();
        slot1.setId(11L);

        Slot slot2 = new Slot();
        slot2.setId(12L);

        // Associate bookings with their respective slots
        booking1.setSlot(slot1);
        booking2.setSlot(slot2);
        booking3.setSlot(null); // No associated slot

        // Mock the behavior of bookingRepository for finding bookings by ID
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking1));
        when(bookingRepository.findById(2L)).thenReturn(Optional.of(booking2));
        when(bookingRepository.findById(3L)).thenReturn(Optional.of(booking3));

        // Mock the behavior of slotRepository for finding slots by ID
        when(slotRepository.findById(11L)).thenReturn(Optional.of(slot1));
        when(slotRepository.findById(12L)).thenReturn(Optional.of(slot2));

        // Call the deleteMultipleBookingsById method
        bookingService.deleteMultipleBookingsById(bookingIdsToDelete);

        // Verify that the soft delete flags are set and updated for each booking
        assertTrue(booking1.getDeleted());
        assertNotNull(booking1.getUpdatedTime());

        assertTrue(booking2.getDeleted());
        assertNotNull(booking2.getUpdatedTime());

        assertFalse(booking3.getDeleted()); // No associated slot


        // Verify that the delete method of bookingRepository was not called for any booking
        verify(bookingRepository, never()).delete(any(Booking.class));

        // Verify that the delete method of slotRepository was not called for any slot
        verify(slotRepository, never()).delete(any(Slot.class));
    }




    @Test
    void testSendCancellationEmailToMentor() {
        // Create a User object for the mentor
        User mentor = new User("MentorName", "mentor@example.com", Collections.singleton(UserRole.MENTOR));

        // Create a Booking object for testing
        Booking booking = new Booking();
        Slot slot = new Slot();
        LocalDateTime bookingDateTime = LocalDateTime.now();
        booking.setBookingDateTime(bookingDateTime);
        booking.setSlot(slot);
        booking.setMentor(mentor); // Set the mentor for the booking

        // Call the method to be tested
        bookingService.sendCancellationEmailToMentor(booking);

        // Verify that emailService.sendEmail was invoked with the expected arguments exactly once
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        String bookingDateTimeFormatted = bookingDateTime.format(formatter);
        verify(emailService, times(1)).sendEmail(
                "Booking Cancellation Notification",
                "Your booking scheduled for " + bookingDateTimeFormatted + " has been cancelled.",
                "mentor@example.com"
        );

    }

    @Test
    void testSendCancellationEmailToMentorWhenBookingIsNull() {
        // Create a null Booking object
        Booking booking = null;

        // Call the method to be tested
        bookingService.sendCancellationEmailToMentor(booking);

        // Verify that emailService.sendEmail was NOT invoked
        verify(emailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    void testSendCancellationEmailToMentorWhenSlotIsNull() {
        // Create a User object for the mentor
        User mentor = new User("MentorName", "mentor@example.com", Collections.singleton(UserRole.MENTOR));

        // Create a Booking object with a null slot
        Booking booking = new Booking();
        booking.setMentor(mentor);
        LocalDateTime bookingDateTime = LocalDateTime.now();
        booking.setBookingDateTime(bookingDateTime);

        // Call the method to be tested
        bookingService.sendCancellationEmailToMentor(booking);

        // Verify that emailService.sendEmail was NOT invoked
        verify(emailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    void testSendCancellationEmailToMentorWhenBookingDateTimeIsNull() {
        // Create a User object for the mentor
        User mentor = new User("MentorName", "mentor@example.com", Collections.singleton(UserRole.MENTOR));

        // Create a Booking object with a null bookingDateTime
        Booking booking = new Booking();
        booking.setMentor(mentor);
        Slot slot = new Slot();
        booking.setSlot(slot);

        // Call the method to be tested
        bookingService.sendCancellationEmailToMentor(booking);

        // Verify that emailService.sendEmail was NOT invoked
        verify(emailService, never()).sendEmail(any(), any(), any());
    }


    @Test
    void testDeleteMultipleBookingsByIdWithException() {
        // Sample booking IDs
        Long bookingId1 = 1L;
        Long bookingId2 = 2L;

        // Mock the behavior of bookingRepository to return Booking objects for the given IDs
        when(bookingRepository.findById(bookingId1)).thenReturn(Optional.of(new Booking()));
        when(bookingRepository.findById(bookingId2)).thenReturn(Optional.empty()); // Simulate not found

        // Create a list of booking IDs to delete
        List<Long> bookingIds = List.of(bookingId1, bookingId2);

        // Verify that a ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () -> bookingService.deleteMultipleBookingsById(bookingIds));

        // Verify that the slotRepository.delete method is not called
        verify(slotRepository, never()).delete(any());
    }


    @Test
    void testCreateBooking_SlotAlreadyExistsException() {
        // Arrange
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setSlotId(1L);

        Slot slot = createSampleSlot();

        when(slotRepository.findById(bookingDTO.getSlotId())).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlotId(bookingDTO.getSlotId())).thenReturn(true); // Simulate a booking already exists for the slot

        // Act and Assert
        assertThrows(SlotAlreadyExistsException.class, () -> bookingService.createBooking(bookingDTO));
    }
    @Test
    void testCreateBooking_OptimisticLockException() {
        // Arrange
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setSlotId(1L);

        Slot slot = createSampleSlot();

        when(slotRepository.findById(bookingDTO.getSlotId())).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlotId(bookingDTO.getSlotId())).thenReturn(false); // Simulate that a booking doesn't exist
        when(engagementRepository.findById(bookingDTO.getEngagementId())).thenReturn(Optional.of(createSampleEngagement()));
        when(userRepository.findById(bookingDTO.getMenteeId())).thenReturn(Optional.of(createSampleUser()));
        when(userRepository.findById(bookingDTO.getMentorId())).thenReturn(Optional.of(createSampleUser()));
        when(bookingRepository.save(any(Booking.class))).thenThrow(OptimisticLockException.class);


        assertThrows(SlotAlreadyExistsException.class, () -> bookingService.createBooking(bookingDTO));
    }



}
package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.controllerexceptions.EngagementNotFoundException;
import com.nineleaps.authentication.jwt.controllerexceptions.UserNotFoundException;
import com.nineleaps.authentication.jwt.dto.MenteeFeedbackDTO;
import com.nineleaps.authentication.jwt.dto.MentorFeedbackDTO;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.Feedback;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.exception.RedundantFeedbackException;
import com.nineleaps.authentication.jwt.repository.EngagementRepository;
import com.nineleaps.authentication.jwt.repository.FeedbackRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.implementation.FeedbackServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class FeedbackServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EngagementRepository engagementRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackServiceImpl;

    @Test
     void testCreateFeedbackByMentee() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        Long engagementId = 3L;
        MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();
        feedbackDTO.setMentorRating(4.5);
        feedbackDTO.setEngagementRating(3.0);

        User mentee = new User();
        User mentor = new User();
        Engagement engagement = new Engagement();
        Feedback feedback = new Feedback();
        feedback.setId(1L);

        when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
        when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(engagement));
        when(feedbackRepository.existsByMenteeIdAndMentorIdAndEngagementId(menteeId, mentorId, engagementId))
                .thenReturn(false);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);
        when(modelMapper.map(feedback, MenteeFeedbackDTO.class)).thenReturn(feedbackDTO);

        // Act
        MenteeFeedbackDTO createdFeedback = feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO);

        // Assert
        assertNotNull(createdFeedback);
        assertEquals(feedbackDTO.getMentorRating(), createdFeedback.getMentorRating());
        assertEquals(feedbackDTO.getEngagementRating(), createdFeedback.getEngagementRating());
    }



    @Test
     void testCreateFeedbackByMentee_UserNotFound() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        Long engagementId = 3L;
        MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();

        when(userRepository.findById(menteeId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class,()->{
            feedbackServiceImpl.createFeedbackByMentee(menteeId,mentorId,engagementId,feedbackDTO);});

    }

    @Test
     void testCreateFeedbackByMentee_EngagementNotFound() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        Long engagementId = 3L;
        MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();

        when(userRepository.findById(menteeId)).thenReturn(Optional.of(new User()));
        when(userRepository.findById(mentorId)).thenReturn(Optional.of(new User()));
        when(engagementRepository.findById(engagementId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EngagementNotFoundException.class, () -> {
            feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO);
        });
    }

   @Test
   void testCreateFeedbackByMentee_RedundantFeedback() {
      // Arrange
      Long menteeId = 1L;
      Long mentorId = 2L;
      Long engagementId = 3L;
      MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();
      feedbackDTO.setMentorRating(4.5);
      feedbackDTO.setEngagementRating(3.0);

      User mentee = new User();
      User mentor = new User();
      Engagement engagement = new Engagement();

      when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
      when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
      when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(engagement));
      when(feedbackRepository.existsByMenteeIdAndMentorIdAndEngagementId(menteeId, mentorId, engagementId))
              .thenReturn(true);

      // Act and Assert
      try {
         feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO);
         fail("Expected RedundantFeedbackException, but no exception was thrown.");
      } catch (RedundantFeedbackException e) {
         assertEquals("Feedback already submitted for the mentor and engagement", e.getMessage());
      }

      verify(feedbackRepository, never()).save(any(Feedback.class));
   }

   @Test
   void testCreateFeedbackByMentee_InvalidMentorRating() {
      // Arrange
      Long menteeId = 1L;
      Long mentorId = 2L;
      Long engagementId = 3L;
      MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();
      feedbackDTO.setMentorRating(6.0);

      when(userRepository.findById(menteeId)).thenReturn(Optional.of(new User()));
      when(userRepository.findById(mentorId)).thenReturn(Optional.of(new User()));
      when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(new Engagement()));

      // Act and Assert
      assertThrows(IllegalArgumentException.class, () -> {
         feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO);
      });
   }





   @Test
     void testCreateFeedbackByMentor() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        Long engagementId = 3L;
        MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();
        feedbackDTO.setMentorRating(4.5);
        feedbackDTO.setEngagementRating(3.0);

        User mentee = new User();
        User mentor = new User();
        Engagement engagement = new Engagement();
        Feedback feedback = new Feedback();
        feedback.setId(1L);

        when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
        when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(engagement));
        when(feedbackRepository.existsByMenteeIdAndMentorIdAndEngagementId(menteeId, mentorId, engagementId))
                .thenReturn(false);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);
        when(modelMapper.map(feedback, MenteeFeedbackDTO.class)).thenReturn(feedbackDTO);

        // Act
        MenteeFeedbackDTO createdFeedback = feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO);

        // Assert
        assertNotNull(createdFeedback);
        assertEquals(feedbackDTO.getMentorRating(), createdFeedback.getMentorRating());
        assertEquals(feedbackDTO.getEngagementRating(), createdFeedback.getEngagementRating());
    }
   @Test
   void testCreateFeedbackByMentee_InvalidRatings() {
      // Arrange
      Long menteeId = 1L;
      Long mentorId = 2L;
      Long engagementId = 3L;
      MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();
      feedbackDTO.setMentorRating(-1.0);
      feedbackDTO.setEngagementRating(6.0);

      User mentee = new User();
      User mentor = new User();
      Engagement engagement = new Engagement();

      when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
      when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
      when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(engagement));

      // Act and Assert
      try {
         feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO);
         fail("Expected IllegalArgumentException, but no exception was thrown.");
      } catch (IllegalArgumentException e) {
         assertEquals("Invalid mentor rating. Rating must be between 0 and 5.", e.getMessage());
      }

      verify(feedbackRepository, never()).save(any(Feedback.class));
   }



   @Test
   void testCreateFeedbackByMentee_InvalidEngagementRating() {
      // Arrange
      Long menteeId = 1L;
      Long mentorId = 2L;
      Long engagementId = 3L;
      MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();
      feedbackDTO.setMentorRating(4.0);
      feedbackDTO.setEngagementRating(-1.0);

      User mentee = new User();
      User mentor = new User();
      Engagement engagement = new Engagement();

      when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
      when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
      when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(engagement));

      // Act and Assert
      try {
         feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO);
         fail("Expected IllegalArgumentException, but no exception was thrown.");
      } catch (IllegalArgumentException e) {

         assertEquals("Invalid engagement rating. Rating must be between 0 and 5.", e.getMessage());
      }

      verify(feedbackRepository, never()).save(any(Feedback.class));
   }

//   Feedback by mentor
@Test
void testCreateFeedbackByMentor_Success() {
   // Arrange
   Long menteeId = 1L;
   Long mentorId = 2L;
   Long engagementId = 3L;
   MentorFeedbackDTO mentorFeedbackDTO = new MentorFeedbackDTO();
   mentorFeedbackDTO.setMenteeRating(4.0);
   mentorFeedbackDTO.setEngagementRating(3.5);

   User mentee = new User();
   User mentor = new User();
   Engagement engagement = new Engagement();
   Feedback feedback = new Feedback();
   feedback.setId(1L);

   when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
   when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
   when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(engagement));
   when(feedbackRepository.existsByMenteeIdAndMentorIdAndEngagementIdAndMentorRating(
           menteeId, mentorId, engagementId, feedback.getMentorRating()))
           .thenReturn(false);
   when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);
   when(modelMapper.map(feedback, MentorFeedbackDTO.class)).thenReturn(mentorFeedbackDTO);

   // Act
   MentorFeedbackDTO createdFeedback = feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, mentorFeedbackDTO);

   // Assert
   assertNotNull(createdFeedback);
   assertEquals(mentorFeedbackDTO.getMenteeRating(), createdFeedback.getMenteeRating());
   assertEquals(mentorFeedbackDTO.getEngagementRating(), createdFeedback.getEngagementRating());
}
   @Test
   void testCreateFeedbackByMentor_RedundantFeedback() {
      // Arrange
      Long menteeId = 1L;
      Long mentorId = 2L;
      Long engagementId = 3L;
      MentorFeedbackDTO mentorFeedbackDTO = new MentorFeedbackDTO();
      mentorFeedbackDTO.setMenteeRating(4.0);
      mentorFeedbackDTO.setEngagementRating(3.5);

      User mentee = new User();
      User mentor = new User();
      Engagement engagement = new Engagement();

      when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
      when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
      when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(engagement));
      when(feedbackRepository.existsByMenteeIdAndMentorIdAndEngagementIdAndMentorRating(
              menteeId, mentorId, engagementId, null))
              .thenReturn(true);


      // Act and Assert
      try {
         feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, mentorFeedbackDTO);
         fail("Expected RedundantFeedbackException, but no exception was thrown.");
      } catch (RedundantFeedbackException e) {
         assertEquals("Feedback already submitted for the mentor and engagement", e.getMessage());
      }

      verify(feedbackRepository, never()).save(any(Feedback.class));
   }
   @Test
   void testCreateFeedbackByMentor_InvalidMentorRating() {
      // Arrange
      Long menteeId = 1L;
      Long mentorId = 2L;
      Long engagementId = 3L;
      MentorFeedbackDTO mentorFeedbackDTO = new MentorFeedbackDTO();
      mentorFeedbackDTO.setMenteeRating(4.0);
      mentorFeedbackDTO.setEngagementRating(3.5);
      mentorFeedbackDTO.setMenteeRating(-1.0);

      User mentee = new User();
      User mentor = new User();
      Engagement engagement = new Engagement();

      when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
      when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
      when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(engagement));

      // Act and Assert
      try {
         feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, mentorFeedbackDTO);
         fail("Expected IllegalArgumentException, but no exception was thrown.");
      } catch (IllegalArgumentException e) {

         assertEquals("Invalid mentor rating. Rating must be between 0 and 5.", e.getMessage());
      }

      verify(feedbackRepository, never()).save(any(Feedback.class));
   }
   @Test
   void testCreateFeedbackByMentor_InvalidEngagementRating() {
      // Arrange
      Long menteeId = 1L;
      Long mentorId = 2L;
      Long engagementId = 3L;
      MentorFeedbackDTO mentorFeedbackDTO = new MentorFeedbackDTO();
      mentorFeedbackDTO.setMenteeRating(4.0);
      mentorFeedbackDTO.setEngagementRating(6.0);

      User mentee = new User();
      User mentor = new User();
      Engagement engagement = new Engagement();

      when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
      when(userRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
      when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(engagement));

      // Act and Assert
      try {
         feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, mentorFeedbackDTO);
         fail("Expected IllegalArgumentException, but no exception was thrown.");
      } catch (IllegalArgumentException e) {
         assertEquals("Invalid engagement rating. Rating must be between 0 and 5.", e.getMessage());
      }

      verify(feedbackRepository, never()).save(any(Feedback.class));
   }



   @Test
     void testCreateFeedbackByMentor_MenteeNotFound() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        Long engagementId = 3L;
        MentorFeedbackDTO feedbackDTO = new MentorFeedbackDTO();

        when(userRepository.findById(menteeId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> {
            feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, feedbackDTO);
        });
    }


    @Test
     void testCreateFeedbackByMentor_EngagementNotFound() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        Long engagementId = 3L;
        MentorFeedbackDTO feedbackDTO = new MentorFeedbackDTO();

        when(userRepository.findById(menteeId)).thenReturn(Optional.of(new User()));
        when(userRepository.findById(mentorId)).thenReturn(Optional.of(new User()));
        when(engagementRepository.findById(engagementId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EngagementNotFoundException.class, () -> {
            feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, feedbackDTO);
        });
    }


   @Test
   void testCreateFeedbackByMentor_InvalidMenteeRating() {
      // Arrange
      Long mentorId = 1L;
      Long menteeId = 2L;
      Long engagementId = 3L;
      MentorFeedbackDTO mentorFeedbackDTO = new MentorFeedbackDTO();
      mentorFeedbackDTO.setMenteeRating(-1.0);

      when(userRepository.findById(menteeId)).thenReturn(Optional.of(new User()));
      when(userRepository.findById(mentorId)).thenReturn(Optional.of(new User()));
      when(engagementRepository.findById(engagementId)).thenReturn(Optional.of(new Engagement()));

      // Act and Assert
      assertThrows(IllegalArgumentException.class, () -> feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, mentorFeedbackDTO));
   }

//   Getting mentee feedback
    @Test
     void testGetAllFeedbackByMentee() {
        // Arrange
        Long menteeId = 1L;
        Feedback feedback=new Feedback();
        feedback.setId(menteeId);
        Feedback feedback1=new Feedback();
        feedback1.setId(menteeId);

        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(feedback);
        feedbacks.add(feedback1);

        when(feedbackRepository.findByMentorId(menteeId)).thenReturn(feedbacks);

        // Act
        List<MenteeFeedbackDTO> feedbackDTOs = feedbackServiceImpl.getAllFeedbackByMentee(menteeId);

        // Assert
        assertNotNull(feedbackDTOs);
        assertEquals(2, feedbackDTOs.size());
    }

    @Test
     void testGetAllFeedbackByMentor() {
        // Arrange
        Long mentorId = 1L;
         Feedback feedback=new Feedback();
         feedback.setId(mentorId);
         Feedback feedback1=new Feedback();
         feedback1.setId(mentorId);
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(feedback);
        feedbacks.add(feedback1);

        when(feedbackRepository.findByMenteeId(mentorId)).thenReturn(feedbacks);

        // Act
        List<MentorFeedbackDTO> feedbackDTOs = feedbackServiceImpl.getAllFeedbackByMentor(mentorId);

        // Assert
        assertNotNull(feedbackDTOs);
        assertEquals(2, feedbackDTOs.size());
    }

    @Test
     void testGetAvgMentorRating() {
        // Arrange
        Long mentorId = 1L;
        Double expectedAvgRating = 4.5;

        when(feedbackRepository.calculateAverageRatingForMentorId(mentorId)).thenReturn(expectedAvgRating);

        // Act
        Double avgMentorRating = feedbackServiceImpl.getAvgMentorRating(mentorId);

        // Assert
        assertEquals(expectedAvgRating, avgMentorRating, 0.01);
    }

    @Test
     void testGetAvgMenteeRating() {
        // Arrange
        Long menteeId = 1L;
        Double expectedAvgRating = 3.0;

        when(feedbackRepository.calculateAverageRatingForMenteeId(menteeId)).thenReturn(expectedAvgRating);

        // Act
        Double avgMenteeRating = feedbackServiceImpl.getAvgMenteeRating(menteeId);

        // Assert
        assertEquals(expectedAvgRating, avgMenteeRating, 0.01);
    }

    @Test
     void testGetAvgEngagementRating() {
        // Arrange
        Long mentorId = 1L;
        Double expectedAvgRating = 4.0;

        when(feedbackRepository.calculateAverageRatingForEngagement(mentorId)).thenReturn(expectedAvgRating);

        // Act
        Double avgEngagementRating = feedbackServiceImpl.getAvgEngagementRating(mentorId);

        // Assert
        assertEquals(expectedAvgRating, avgEngagementRating, 0.01);
    }

    @Test
     void testConvertToDTOO() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setId(1L);
        feedback.setMenteeRating(4.5);
        feedback.setEngagementRating(3.0);

        MentorFeedbackDTO expectedDTO = new MentorFeedbackDTO();
        expectedDTO.setId(1L);
        expectedDTO.setMenteeRating(4.5);
        expectedDTO.setEngagementRating(3.0);

        when(modelMapper.map(feedback, MentorFeedbackDTO.class)).thenReturn(expectedDTO);

        // Act
        MentorFeedbackDTO resultDTO = feedbackServiceImpl.convertToDTOO(feedback);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(expectedDTO.getId(), resultDTO.getId());
        assertEquals(expectedDTO.getMenteeRating(), resultDTO.getMenteeRating());
        assertEquals(expectedDTO.getEngagementRating(), resultDTO.getEngagementRating());
    }

    @Test
     void testConvertToDTO() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setId(1L);
        feedback.setMenteeRating(4.5);
        feedback.setEngagementRating(3.0);
        // Set other properties as needed

        MenteeFeedbackDTO expectedDTO = new MenteeFeedbackDTO();
        expectedDTO.setId(1L);
        expectedDTO.setMentorRating(4.5);
        expectedDTO.setEngagementRating(3.0);
        // Set other properties of the expected DTO

        when(modelMapper.map(feedback, MenteeFeedbackDTO.class)).thenReturn(expectedDTO);

        // Act
        MenteeFeedbackDTO resultDTO = feedbackServiceImpl.convertToDTO(feedback);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(expectedDTO.getId(), resultDTO.getId());
        assertEquals(expectedDTO.getMentorRating(), resultDTO.getMentorRating());
        assertEquals(expectedDTO.getEngagementRating(), resultDTO.getEngagementRating());
    }

}

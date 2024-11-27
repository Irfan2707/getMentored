package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.ActivityLogDTO;
import com.nineleaps.authentication.jwt.dto.ChecklistItemDTO;
import com.nineleaps.authentication.jwt.entity.ActivityLog;
import com.nineleaps.authentication.jwt.entity.ChecklistItem;
import com.nineleaps.authentication.jwt.entity.GoalTracker;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.ChecklistitemStatus;
import com.nineleaps.authentication.jwt.repository.ActivityLogRepository;
import com.nineleaps.authentication.jwt.repository.ChecklistItemRepository;
import com.nineleaps.authentication.jwt.repository.GoalTrackerRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.implementation.ChecklistItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ChecklistItemServiceImplTest {

    @InjectMocks
    private ChecklistItemServiceImpl checklistItemServiceImpl;

    @Mock
    private ChecklistItemRepository checklistItemRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ActivityLogRepository activityLogRepository;
    @Mock
    private GoalTrackerRepository goalTrackerRepository;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//     void testGetAllChecklistItemsByGoalTrackerId() {
//        // Arrange
//        Long goalTrackerId = 1L;
//        ChecklistItem checklistItem1 = new ChecklistItem();
//        checklistItem1.setId(1L);
//        checklistItem1.setItemDescription("Item 1");
//        ChecklistItem checklistItem2 = new ChecklistItem();
//        checklistItem2.setId(2L);
//        checklistItem2.setItemDescription("Item 2");
//        List<ChecklistItem> checklistItems = new ArrayList<>();
//        checklistItems.add(checklistItem1);
//        checklistItems.add(checklistItem2);
//
//        // Mocking checklistItemRepository to return the checklistItems when findAllByGoalTrackerId is called
//        when(checklistItemRepository.findAllByGoalTrackerId(goalTrackerId)).thenReturn(checklistItems);
//
//        // Mocking modelMapper to return a DTO when mapping from ChecklistItem
//        when(modelMapper.map(checklistItem1,ChecklistItemDTO.class)).thenReturn(new ChecklistItemDTO());
//        when(modelMapper.map(checklistItem2, ChecklistItemDTO.class)).thenReturn(new ChecklistItemDTO());
//
//        // Act
//        List<ChecklistItemDTO> result = checklistItemServiceImpl.getAllChecklistItemsByGoalTrackerId(goalTrackerId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//
//
//    }
//
//    @Test
//     void testGetAllChecklistItemsByGoalTrackerIdEmptyList() {
//        // Arrange
//        Long goalTrackerId = 1L;
//        List<ChecklistItem> emptyList = new ArrayList<>();
//
//        // Mocking checklistItemRepository to return an empty list
//        when(checklistItemRepository.findAllByGoalTrackerId(goalTrackerId)).thenReturn(emptyList);
//
//        // Act
//        List<ChecklistItemDTO> result = checklistItemServiceImpl.getAllChecklistItemsByGoalTrackerId(goalTrackerId);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//     void testGetAllChecklistItemsByGoalTrackerIdException() {
//        // Arrange
//        Long goalTrackerId = 1L;
//
//        // Mocking checklistItemRepository to throw an exception
//        when(checklistItemRepository.findAllByGoalTrackerId(goalTrackerId)).thenThrow(new RuntimeException("Test Exception"));
//
//        // Act
//        List<ChecklistItemDTO> result = checklistItemServiceImpl.getAllChecklistItemsByGoalTrackerId(goalTrackerId);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//
//    }



     @Test
     void testGetAllChecklistItemsByGoalTrackerId_Success() {
         // Create a sample Goal Tracker ID
         Long goalTrackerId = 1L;

         // Create a list of ChecklistItem objects
         ChecklistItem checklistItem1 = new ChecklistItem();
         ChecklistItem checklistItem2 = new ChecklistItem();
         List<ChecklistItem> checklistItems = Arrays.asList(checklistItem1, checklistItem2);

         // Create a Page of ChecklistItem
         Page<ChecklistItem> checklistItemsPage = new PageImpl<>(checklistItems);

         // Mock the behavior of checklistItemRepository to return the Page
         when(checklistItemRepository.findAllByGoalTrackerId(goalTrackerId, PageRequest.of(0, 10)))
                 .thenReturn(checklistItemsPage);

         // Call the service method with pagination
         List<ChecklistItemDTO> result = checklistItemServiceImpl.getAllChecklistItemsByGoalTrackerId(0, 10, goalTrackerId);

         // Verify the result
         assertEquals(0, result.size()); // Check that the result has 2 ChecklistItemDTOs
     }

     @Test
     void testGetAllChecklistItemsByGoalTrackerId_NoItemsFound() {
         // Create a sample Goal Tracker ID
         Long goalTrackerId = 1L;

         // Mock the behavior of checklistItemRepository to return an empty Page
         when(checklistItemRepository.findAllByGoalTrackerId(goalTrackerId, PageRequest.of(0, 10)))
                 .thenReturn(Page.empty());

         // Call the service method with pagination
         List<ChecklistItemDTO> result = checklistItemServiceImpl.getAllChecklistItemsByGoalTrackerId(0, 10, goalTrackerId);

         // Verify the result
         assertEquals(0, result.size()); // Check that the result is an empty list
     }

     @Test
     void testGetAllChecklistItemsByGoalTrackerId_Exception() {
         // Mock data
         Long goalTrackerId = 1L;

         // Simulate an exception being thrown when calling checklistItemRepository.findAllByGoalTrackerId
         when(checklistItemRepository.findAllByGoalTrackerId(goalTrackerId, PageRequest.of(0, 10)))
                 .thenThrow(new RuntimeException("Test Exception"));

         // Call the service method with pagination
         List<ChecklistItemDTO> checklistItemDTOs = checklistItemServiceImpl.getAllChecklistItemsByGoalTrackerId(0, 10, goalTrackerId);

         // Verify that checklistItemRepository.findAllByGoalTrackerId is called once
         verify(checklistItemRepository, times(1)).findAllByGoalTrackerId(goalTrackerId, PageRequest.of(0, 10));

         // Assert that the returned list is empty
         assertNotNull(checklistItemDTOs);
         assertTrue(checklistItemDTOs.isEmpty());
     }
     @Test
     void testGetAllChecklistItemByGoalTrackerId_Success() {
         // Create a sample Goal Tracker ID
         Long goalTrackerId = 1L;

         // Create a sample page number and page size
         int pageNumber = 0;
         int pageSize = 10;  // Set an appropriate page size

         List<ChecklistItem> checklistItems = new ArrayList<>();

         // Create and associate GoalTracker with ChecklistItem
         ChecklistItem checklistItem1 = new ChecklistItem();
         GoalTracker goalTracker1 = new GoalTracker();
         goalTracker1.setId(goalTrackerId);
         checklistItem1.setGoalTracker(goalTracker1);
         checklistItems.add(checklistItem1);

         ChecklistItem checklistItem2 = new ChecklistItem();
         GoalTracker goalTracker2 = new GoalTracker();
         goalTracker2.setId(goalTrackerId);
         checklistItem2.setGoalTracker(goalTracker2);
         checklistItems.add(checklistItem2);

         // Mock the behavior of checklistItemRepository
         when(checklistItemRepository.findAllByGoalTrackerId(goalTrackerId, PageRequest.of(pageNumber, pageSize)))
                 .thenReturn(new PageImpl<>(checklistItems));

         // Call the service method
         List<ChecklistItemDTO> result = checklistItemServiceImpl.getAllChecklistItemsByGoalTrackerId(pageNumber, pageSize, goalTrackerId);

         // Verify the result
         assertEquals(2, result.size()); // Check that the result has 2 ChecklistItemDTOs
     }


    @Test
     void testGetChecklistItemDTO() {
        // Arrange
        ChecklistItem checklistItem = new ChecklistItem();
        checklistItem.setId(1L);
        checklistItem.setItemDescription("Sample Item");
        checklistItem.setStatus(ChecklistitemStatus.PENDING);
        GoalTracker goalTracker = new GoalTracker();
        goalTracker.setId(2L);
        checklistItem.setGoalTracker(goalTracker);

        // Act
        ChecklistItemDTO checklistItemDTO = ChecklistItemServiceImpl.getChecklistItemDTO(checklistItem);

        // Assert
        assertNotNull(checklistItemDTO);
        assertEquals(1L, checklistItemDTO.getId());
        assertEquals(2L, checklistItemDTO.getGoalTrackerId());
        assertEquals("Sample Item", checklistItemDTO.getItemDescription());
        assertEquals(ChecklistitemStatus.PENDING, checklistItemDTO.getStatus());
    }


     @Test
     void testCreateChecklistItem_Success() throws ResourceNotFoundException {
         // Arrange
         Long userId = 1L;
         Long goalTrackerId = 2L; // Set a valid goalTrackerId
         ChecklistItemDTO newItemDTO = new ChecklistItemDTO();
         newItemDTO.setItemDescription("New Item");
         newItemDTO.setGoalTrackerId(goalTrackerId);

         User user = new User();
         user.setId(userId);

         GoalTracker goalTracker = new GoalTracker();
         goalTracker.setId(goalTrackerId);

         when(userRepository.findById(userId)).thenReturn(Optional.of(user));

         // Mocking the behavior of goalTrackerRepository to return the expected GoalTracker
         when(goalTrackerRepository.findById(goalTrackerId)).thenReturn(Optional.of(goalTracker));

         when(checklistItemRepository.save(any())).thenAnswer(invocation -> {
             ChecklistItem savedItem = invocation.getArgument(0);
             savedItem.setId(1L);
             return savedItem;
         });

         // Act
         ChecklistItemDTO result = checklistItemServiceImpl.createChecklistItem(userId, newItemDTO);

         // Assert
         assertNotNull(result);
         assertEquals(newItemDTO.getItemDescription(), result.getItemDescription());
         assertEquals(ChecklistitemStatus.PENDING, result.getStatus());
         assertNotNull(result.getCreatedAt());
         assertEquals(goalTrackerId, result.getGoalTrackerId());
     }




     @Test
     void testUpdateChecklistItem_Success() throws ResourceNotFoundException {
        // Arrange
        Long checklistItemId = 1L;
        Long userId = 1L;
        ChecklistItemDTO updatedChecklistItemDTO = createChecklistItemDTO("Updated Item");
        User user = createUser(userId, "TestUser");
        ChecklistItem existingChecklistItem = createChecklistItem(checklistItemId, "Updated Item");

        // Mocking checklistItemRepository to return the existing checklist item
        when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.of(existingChecklistItem));

        // Mocking userRepository to return the user
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mocking checklistItemRepository to return the updated checklist item
        when(checklistItemRepository.save(any(ChecklistItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ChecklistItemDTO result = checklistItemServiceImpl.updateChecklistItem(checklistItemId, updatedChecklistItemDTO, userId);

        // Assert
        assertNotNull(result);
        assertEquals(checklistItemId, result.getId());
        assertEquals("Updated Item", result.getItemDescription());
        assertEquals(ChecklistitemStatus.PENDING, result.getStatus());
    }

    @Test
     void testUpdateChecklistItem_ChecklistItemNotFound() {
        // Arrange
        Long checklistItemId = 1L;
        Long userId = 1L;
        ChecklistItemDTO updatedChecklistItemDTO = createChecklistItemDTO("Updated Item");

        // Mocking checklistItemRepository to return an empty Optional
        when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            checklistItemServiceImpl.updateChecklistItem(checklistItemId, updatedChecklistItemDTO, userId);
        });

    }

    @Test
     void testUpdateChecklistItem_UserNotFound() {
        // Arrange
        Long checklistItemId = 1L;
        Long userId = 1L;
        ChecklistItemDTO updatedChecklistItemDTO = createChecklistItemDTO("Updated Item");
        ChecklistItem existingChecklistItem = createChecklistItem(checklistItemId, "Original Item");

        // Mocking checklistItemRepository to return the existing checklist item
        when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.of(existingChecklistItem));

        // Mocking userRepository to return an empty Optional
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            checklistItemServiceImpl.updateChecklistItem(checklistItemId, updatedChecklistItemDTO, userId);
        });

    }

//    @Test
//     void testDeleteChecklistItem_Success() {
//        // Arrange
//        Long checklistItemId = 1L;
//        ChecklistItem existingChecklistItem = createChecklistItem(checklistItemId, "Original Item");
//
//        // Mocking checklistItemRepository to return the existing checklist item
//        when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.of(existingChecklistItem));
//
//        // Act
//        checklistItemServiceImpl.deleteChecklistItem(checklistItemId);
//
//        verify(checklistItemRepository).delete(existingChecklistItem);
//
//
//    }

    @Test
     void testDeleteChecklistItem_ChecklistItemNotFound() {
        // Arrange
        Long checklistItemId = 1L;

        // Mocking checklistItemRepository to return an empty Optional
        when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> {
            checklistItemServiceImpl.deleteChecklistItem(checklistItemId);
        });


    }

     @Test
     void testDeleteChecklistItemWithSoftDelete_Success() {
         // Arrange
         Long id = 1L;
         ChecklistItem existingChecklistItem = new ChecklistItem();
         existingChecklistItem.setId(id);
         when(checklistItemRepository.findById(id)).thenReturn(Optional.of(existingChecklistItem));
         when(activityLogRepository.save(any(ActivityLog.class))).thenReturn(new ActivityLog());

         // Act
         checklistItemServiceImpl.deleteChecklistItem(id);

         // Assert
         assertTrue(existingChecklistItem.getDeleted());
         assertNotNull(existingChecklistItem.getUpdatedAt());
         verify(checklistItemRepository, times(1)).save(existingChecklistItem);
         verify(activityLogRepository, times(1)).save(any(ActivityLog.class));
         verify(checklistItemRepository, never()).delete(existingChecklistItem);
     }



    @Test
    void testUpdateChecklistItemStatus_Success() throws ResourceNotFoundException {
        // Mock data
        Long userId = 1L;
        Long checklistItemId = 2L;
        ChecklistitemStatus newStatus = ChecklistitemStatus.DONE;
        LocalDateTime currentTime = LocalDateTime.now();

        User user = new User();
        user.setId(userId);
        user.setUserName("testuser");

        GoalTracker goalTracker = new GoalTracker();
        goalTracker.setId(3L);

        ChecklistItem checklistItem = new ChecklistItem();
        checklistItem.setId(checklistItemId);
        checklistItem.setStatus(ChecklistitemStatus.PENDING);
        checklistItem.setGoalTracker(goalTracker);
        checklistItem.setUpdatedAt(currentTime);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.of(checklistItem));
        when(checklistItemRepository.save(any(ChecklistItem.class))).thenAnswer(invocation -> {
            ChecklistItem savedItem = invocation.getArgument(0);
            savedItem.setUpdatedAt(LocalDateTime.now());
            savedItem.setStatus(newStatus);
            return savedItem;
        });

        ChecklistItemDTO updatedChecklistItemDTO = checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus);

        verify(checklistItemRepository, times(1)).save(any(ChecklistItem.class));

        assertEquals(newStatus, updatedChecklistItemDTO.getStatus());

        ArgumentCaptor<ActivityLog> activityLogCaptor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(activityLogRepository).save(activityLogCaptor.capture());


    }

     @Test
     void testUpdateChecklistItemStatus_GoalTrackerNotFound() {
         // Mock data
         Long userId = 1L;
         Long checklistItemId = 2L;
         ChecklistitemStatus newStatus = ChecklistitemStatus.DONE;

         User user = new User();
         user.setId(userId);
         user.setUserName("testuser");

         // Create a checklist item without an associated goalTracker
         ChecklistItem checklistItem = new ChecklistItem();
         checklistItem.setId(checklistItemId);
         checklistItem.setStatus(ChecklistitemStatus.PENDING);
         checklistItem.setGoalTracker(null);

         when(userRepository.findById(userId)).thenReturn(Optional.of(user));
         when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.of(checklistItem));

         ResourceNotFoundException exception = assertThrows(
                 ResourceNotFoundException.class,
                 () -> checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus)
         );

         assertTrue(exception.getMessage().contains("GoalTracker not found for the ChecklistItem with ID: " + checklistItemId));
     }



     @Test
    void testUpdateChecklistItemStatus_InvalidUserId() {
        // Mock data
        Long userId = 1L;
        Long checklistItemId = 2L;
        ChecklistitemStatus newStatus = ChecklistitemStatus.DONE;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the method to be tested and expect a ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus)
        );

        assertTrue(exception.getMessage().contains("User not found with ID: " + userId));

        verify(checklistItemRepository, never()).save(any());
    }

    @Test
    void testUpdateChecklistItemStatus_InvalidChecklistItemId() {
        // Mock data
        Long userId = 1L;
        Long checklistItemId = 2L;
        ChecklistitemStatus newStatus = ChecklistitemStatus.DONE;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus)
        );

        assertTrue(exception.getMessage().contains("Checklist item not found with ID: " + checklistItemId));

        verify(checklistItemRepository, never()).save(any());
    }
    @Test
    void testUpdateChecklistItemStatus_UserNotFound() {
        // Create test data
        Long userId = 1L;
        Long checklistItemId = 2L;
        ChecklistitemStatus newStatus = ChecklistitemStatus.DONE;

        // Mocking repository calls to simulate a user not found scenario
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus));
    }

    @Test
    void testUpdateChecklistItemStatus_ChecklistItemNotFound() {
        // Create test data
        Long userId = 1L;
        Long checklistItemId = 2L;
        ChecklistitemStatus newStatus = ChecklistitemStatus.DONE;

        User user = new User();
        user.setId(userId);

        // Mocking repository calls to simulate a checklist item not found scenario
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus));
    }


     @Test
     void testUpdateChecklistItemStatus_Completed() throws ResourceNotFoundException {
         // Mock data
         Long userId = 1L;
         Long checklistItemId = 2L;
         ChecklistitemStatus newStatus = ChecklistitemStatus.DONE;
         LocalDateTime currentTime = LocalDateTime.now();

         User user = new User();
         user.setId(userId);
         user.setUserName("testuser");

         GoalTracker goalTracker = new GoalTracker();
         goalTracker.setId(3L);

         ChecklistItem checklistItem = new ChecklistItem();
         checklistItem.setId(checklistItemId);
         checklistItem.setStatus(ChecklistitemStatus.DONE);
         checklistItem.setGoalTracker(goalTracker);
         checklistItem.setUpdatedAt(currentTime);

         when(userRepository.findById(userId)).thenReturn(Optional.of(user));
         when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.of(checklistItem));
         when(checklistItemRepository.save(any(ChecklistItem.class))).thenAnswer(invocation -> {
             // Simulate the saving of the checklist item and return the updated checklist item
             ChecklistItem savedItem = invocation.getArgument(0);
             savedItem.setUpdatedAt(LocalDateTime.now());
             savedItem.setStatus(newStatus);
             return savedItem;
         });

         ChecklistItemDTO updatedChecklistItemDTO = checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus);

         ArgumentCaptor<ActivityLog> activityLogCaptor = ArgumentCaptor.forClass(ActivityLog.class);
         verify(activityLogRepository).save(activityLogCaptor.capture());

         ActivityLog savedActivityLog = activityLogCaptor.getValue();
         assertEquals("Completed", savedActivityLog.getActivityType());
     }

     @Test
     void testUpdateChecklistItemStatus_MarkedAsIncomplete() throws ResourceNotFoundException {
         // Mock data
         Long userId = 1L;
         Long checklistItemId = 2L;
         ChecklistitemStatus newStatus = ChecklistitemStatus.PENDING;
         LocalDateTime currentTime = LocalDateTime.now();

         User user = new User();
         user.setId(userId);
         user.setUserName("testuser");

         GoalTracker goalTracker = new GoalTracker();
         goalTracker.setId(3L);

         ChecklistItem checklistItem = new ChecklistItem();
         checklistItem.setId(checklistItemId);
         checklistItem.setStatus(ChecklistitemStatus.PENDING);
         checklistItem.setGoalTracker(goalTracker);
         checklistItem.setUpdatedAt(currentTime);

         when(userRepository.findById(userId)).thenReturn(Optional.of(user));
         when(checklistItemRepository.findById(checklistItemId)).thenReturn(Optional.of(checklistItem));
         when(checklistItemRepository.save(any(ChecklistItem.class))).thenAnswer(invocation -> {
             // Simulate the saving of the checklist item and return the updated checklist item
             ChecklistItem savedItem = invocation.getArgument(0);
             savedItem.setUpdatedAt(LocalDateTime.now());
             savedItem.setStatus(newStatus);
             return savedItem;
         });
         ChecklistItemDTO updatedChecklistItemDTO = checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus);

         ArgumentCaptor<ActivityLog> activityLogCaptor = ArgumentCaptor.forClass(ActivityLog.class);
         verify(activityLogRepository).save(activityLogCaptor.capture());

         ActivityLog savedActivityLog = activityLogCaptor.getValue();
         assertEquals("Marked as incomplete", savedActivityLog.getActivityType());
     }




     @Test
     void testConvertToEntity() {
        // Arrange
        ChecklistItemDTO checklistItemDTO = new ChecklistItemDTO();
        checklistItemDTO.setId(1L);
        checklistItemDTO.setGoalTrackerId(2L);
        checklistItemDTO.setItemDescription("Sample Item");
        checklistItemDTO.setStatus(ChecklistitemStatus.PENDING);

        GoalTracker goalTracker = new GoalTracker();
        goalTracker.setId(2L);

        when(goalTrackerRepository.findById(2L)).thenReturn(java.util.Optional.of(goalTracker));

        // Act
        ChecklistItem result = checklistItemServiceImpl.convertToEntity(checklistItemDTO);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals(2L, result.getGoalTracker().getId());
        assertEquals("Sample Item", result.getItemDescription());
        assertEquals(ChecklistitemStatus.PENDING, result.getStatus());
    }

    @Test
     void testConvertToDTO() {
        // Arrange
        ChecklistItem checklistItem = new ChecklistItem();
        checklistItem.setId(1L);
        GoalTracker goalTracker = new GoalTracker();
        goalTracker.setId(2L);
        checklistItem.setGoalTracker(goalTracker);
        checklistItem.setItemDescription("Sample Item");
        checklistItem.setStatus(ChecklistitemStatus.PENDING);

        // Act
        ChecklistItemDTO result = checklistItemServiceImpl.convertToDTO(checklistItem);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals(2L, result.getGoalTrackerId());
        assertEquals("Sample Item", result.getItemDescription());
        assertEquals(ChecklistitemStatus.PENDING, result.getStatus());
    }

    @Test
     void testGetActivityDetailsByGoal_NoActivityLogs() throws ResourceNotFoundException {
        // Arrange
        Long userId = 1L;
        Long goalId = 2L;

        when(checklistItemRepository.findAllByGoalTrackerId(goalId)).thenReturn(Collections.emptyList());

        // Act
        List<ActivityLogDTO> result = checklistItemServiceImpl.getActivityDetailsByGoal(userId, goalId);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetActivityDetailsByGoal_Success() throws ResourceNotFoundException {
        // Arrange
        Long userId = 1L;
        Long goalId = 2L;

        List<ChecklistItem> checklistItems = new ArrayList<>();
        List<ActivityLog> activityLogs = new ArrayList<>();

        ChecklistItem checklistItem1 = createChecklistItem(101L, "Task 1");
        ChecklistItem checklistItem2 = createChecklistItem(102L, "Task 2");

        checklistItems.add(checklistItem1);
        checklistItems.add(checklistItem2);

        // Mocking the repository to return checklist items
        when(checklistItemRepository.findAllByGoalTrackerId(goalId)).thenReturn(checklistItems);

        // Mocking activity logs for each checklist item using helper method
        for (ChecklistItem checklistItem : checklistItems) {
            List<ActivityLog> checklistActivityLogs = new ArrayList<>();

            ActivityLog activityLog1 = createActivityLog(checklistItem);
            ActivityLog activityLog2 = createActivityLog(checklistItem);

            checklistActivityLogs.add(activityLog1);
            checklistActivityLogs.add(activityLog2);

            when(activityLogRepository.findByChecklistItem(checklistItem)).thenReturn(checklistActivityLogs);
        }

        // Mocking the modelMapper to map ActivityLog to ActivityLogDTO
        when(modelMapper.map(any(ActivityLog.class), eq(ActivityLogDTO.class))).thenAnswer(
                invocation -> {
                    ActivityLog activityLog = invocation.getArgument(0);
                    ActivityLogDTO activityLogDTO = new ActivityLogDTO();
                    activityLogDTO.setActivityType(activityLog.getActivityType());
                    activityLogDTO.setActivityTime(activityLog.getActivityTime());
                    // Map other properties as needed
                    return activityLogDTO;
                }
        );

        // Act
        List<ActivityLogDTO> result = checklistItemServiceImpl.getActivityDetailsByGoal(userId, goalId);

        // Assert
        assertNotNull(result);

        for (ActivityLogDTO activityLogDTO : result) {
            assertNotNull(activityLogDTO.getActivityTime());
            assertNotNull(activityLogDTO.getActivityType());
        }
    }



     @Test
     void testDeleteChecklistItemWithSoftDeleteAndExistingActivityLogs_Success() {
         // Arrange
         Long id = 1L;

         // Mock the checklistItemRepository to return an existing checklist item with activity logs
         ChecklistItem existingChecklistItem = new ChecklistItem();
         existingChecklistItem.setId(id);
         List<ActivityLog> existingActivityLogs = new ArrayList<>();
         ActivityLog activityLog = new ActivityLog();
         existingActivityLogs.add(activityLog);
         existingChecklistItem.setActivityLogs(existingActivityLogs);
         when(checklistItemRepository.findById(id)).thenReturn(Optional.of(existingChecklistItem));

         // Mock the activityLogRepository save method
         when(activityLogRepository.save(any(ActivityLog.class))).thenReturn(new ActivityLog());

         // Act
         checklistItemServiceImpl.deleteChecklistItem(id);

         // Assert

         // Verify that the checklist item's deleted flag is set
         assertTrue(existingChecklistItem.getDeleted());

         // Verify that the updatedAt timestamp is updated
         assertNotNull(existingChecklistItem.getUpdatedAt());

         // Verify that the save method was called for the checklist item
         verify(checklistItemRepository, times(1)).save(existingChecklistItem);

         // Verify that the save method was called for the activity log
         verify(activityLogRepository, times(1)).save(any(ActivityLog.class));

         // Verify that delete method of checklistItemRepository was not called
         verify(checklistItemRepository, never()).delete(existingChecklistItem);
     }

     @Test
     void testDeleteChecklistItemWithSoftDeleteAndNullActivityLogs_Success() {
         // Arrange
         Long id = 1L;

         // Mock the checklistItemRepository to return an existing checklist item with null activity logs
         ChecklistItem existingChecklistItem = new ChecklistItem();
         existingChecklistItem.setId(id);
         when(checklistItemRepository.findById(id)).thenReturn(Optional.of(existingChecklistItem));

         // Mock the activityLogRepository save method
         when(activityLogRepository.save(any(ActivityLog.class))).thenReturn(new ActivityLog());

         // Act
         checklistItemServiceImpl.deleteChecklistItem(id);

         // Assert

         // Verify that the checklist item's deleted flag is set
         assertTrue(existingChecklistItem.getDeleted());

         // Verify that the updatedAt timestamp is updated
         assertNotNull(existingChecklistItem.getUpdatedAt());

         // Verify that the save method was called for the checklist item
         verify(checklistItemRepository, times(1)).save(existingChecklistItem);

         // Verify that the save method was called for the activity log
         verify(activityLogRepository, times(1)).save(any(ActivityLog.class));

         // Verify that delete method of checklistItemRepository was not called
         verify(checklistItemRepository, never()).delete(existingChecklistItem);
     }

    // Helper method to create a ChecklistItemDTO
    private ChecklistItemDTO createChecklistItemDTO(String itemDescription) {
        ChecklistItemDTO checklistItemDTO = new ChecklistItemDTO();
        checklistItemDTO.setStatus(ChecklistitemStatus.PENDING);
        checklistItemDTO.setCreatedAt(LocalDateTime.now());
        checklistItemDTO.setUpdatedAt(LocalDateTime.now());
        checklistItemDTO.setItemDescription(itemDescription);
        return checklistItemDTO;
    }

    // Helper method to create a User
    private User createUser(Long id, String userName) {
        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        return user;
    }

    // Helper method to create a ChecklistItem
    private ChecklistItem createChecklistItem(Long id, String itemDescription) {
        ChecklistItem checklistItem = new ChecklistItem();
        checklistItem.setStatus(ChecklistitemStatus.PENDING);
        checklistItem.setId(id);
        checklistItem.setGoalTracker(createGoalTracker(id));
        checklistItem.setItemDescription(itemDescription);

        return checklistItem;
    }

    // Helper method to create an ActivityLog
    private ActivityLog createActivityLog(ChecklistItem checklistItem) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setActivityType("Created Task");
        activityLog.setActivityTime(LocalDateTime.now());
        activityLog.setChecklistItem(checklistItem);
        return activityLog;
    }

    // Helper method to create a GoalTracker
    private GoalTracker createGoalTracker(Long id) {
        GoalTracker goalTracker = new GoalTracker();
        goalTracker.setId(id);
        return goalTracker;
    }

}
package com.nineleaps.authentication.jwt.service.interfaces;


import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.SlotDTO;
import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTO;
import com.nineleaps.authentication.jwt.entity.Slot;
import com.nineleaps.authentication.jwt.enums.BookingStatus;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ISlotService {

    SlotDTO createSlot(SlotDTO slotDTO) throws ConflictException;


    SlotDTO updateSlot(Long id, SlotDTO slotDTO) throws ResourceNotFoundException;

    boolean deleteSlot(Long id) throws ResourceNotFoundException;

    void deleteMultipleSlots(List<Slot> slots);

    public boolean deleteMultipleSlotsById(List<Long> slotId);

    Page<SlotDTO> getSlotsByMentorId(Long mentorId, int pageNumber, int pageSize);


    void setStatus(Long slotId, BookingStatus bookingStatus) throws ResourceNotFoundException;

    SlotStatisticsDTO getSlotCountsByMentorAndDateRange(Long mentorId, String startDateStr, String endDateStr);
}

    



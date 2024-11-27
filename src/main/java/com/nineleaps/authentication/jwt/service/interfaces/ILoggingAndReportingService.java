package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.dto.ConnectionRequestStatisticsMentorDTO;
import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTO;

public interface ILoggingAndReportingService {

    /**
     * Get Connection Request Statistics for a Mentor based on Mentor ID.
     *
     * @param mentorId The ID of the Mentor for whom to retrieve Connection Request Statistics.
     * @return Connection Request Statistics for the specified Mentor.
     */
    ConnectionRequestStatisticsMentorDTO getConnectionsStatisticsByMentorId(Long mentorId);

    /**
     * Get Slot Statistics for a Mentor based on Mentor ID.
     *
     * @param mentorId The ID of the Mentor for whom to retrieve Slot Statistics.
     * @return Slot Statistics for the specified Mentor.
     */
    SlotStatisticsDTO getSlotStatisticsByMentorId(Long mentorId);
}

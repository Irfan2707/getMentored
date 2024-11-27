package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.dto.MenteeDTO;
import com.nineleaps.authentication.jwt.dto.MentorDTO;

public interface IProfileService {
    String uploadImage(String userMail, String base64Image);

    void updateMenteeProfile(MenteeDTO menteedto);

    void updateMentorProfile(MentorDTO mentordto);
}

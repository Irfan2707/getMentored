package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;

import java.util.List;

public interface ISearchForMentor {

    //search for user using name
    public List<User> getUserByName(String name);


    //search for user using expertise
    public List<User> getUserByExpertise(String expertise);

    //search for user using industry
    List<User> getUserByIndustry(String industry);

    //search for user using Role
    List<MentorDTO> getUsersByRole(UserRole role);

    //search for user using name or expertise or industry
    List<User> getUserByNameOrExpertiseOrIndustry(String name, String expertise, String industry);

    //Getting user by keyword(Expertise or industry)
    //Search for user using single keyword
    List<User> getUsersByKeyword(String keyword);

    //Search for user using mentordto class
    List<MentorDTO> getByMentorDto(String keyword);

    List<MentorDTO> getSortedMentors(
            String expertise,
            String industry,
            String location,
            Long menteeId
    );

}
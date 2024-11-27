package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.repository.ConnectionRequestRepo;
import com.nineleaps.authentication.jwt.repository.FeedbackRepository;
import com.nineleaps.authentication.jwt.repository.SearchRepository;
import com.nineleaps.authentication.jwt.service.interfaces.ISearchForMentor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchForMentorServiceImpl implements ISearchForMentor {
    @Autowired
    private final SearchRepository searchRepository;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final FeedbackRepository feedbackRepository;
    @Autowired
    private final ConnectionRequestRepo connectionRequestRepository;
    private static final Logger logger = LoggerFactory.getLogger(SearchForMentorServiceImpl.class);

    @Override
    public List<User> getUserByName(String name) {
        logger.info("Searching for users by name: {}", name);
        return searchRepository.findByuserNameContainingIgnoreCase(name);
    }

    @Override
    public List<User> getUserByExpertise(String expertise) {
        logger.info("Searching for users by expertise: {}", expertise);
        return searchRepository.findByExpertiseContaining(expertise);
    }

    @Override
    public List<User> getUserByIndustry(String industry) {
        logger.info("Searching for users by industry: {}", industry);
        return searchRepository.findByIndustryContainingIgnoreCase(industry);
    }

    @Override
    public List<MentorDTO> getUsersByRole(UserRole role) {
        logger.info("Searching for users by role: {}", role);
        List<User> users = searchRepository.findByRoles(role);
        List<MentorDTO> mentorDTOList = new ArrayList<>();
        for (User user : users) {
            logger.info("User found with ID: {}", user.getId());
            mentorDTOList.add(modelMapper.map(user, MentorDTO.class));
        }
        return mentorDTOList;
    }

    @Override
    public List<User> getUserByNameOrExpertiseOrIndustry(String name, String expertise, String industry) {
        logger.info("Searching for users by name: {}, expertise: {}, and industry: {}", name, expertise, industry);
        return searchRepository.findByuserNameOrExpertiseOrIndustry(name, expertise, industry);
    }

    @Override
    public List<User> getUsersByKeyword(String keyword) {
        logger.info("Searching for users by keyword: {}", keyword);
        return searchRepository.findByKeyword(keyword);
    }

    @Override
    public List<MentorDTO> getByMentorDto(String keyword) {
        logger.info("Getting mentors by mentor DTO with keyword: {}", keyword);
        List<User> users = searchRepository.findByMentorDto(keyword);
        List<MentorDTO> mentorDTOs = new ArrayList<>();

        for (User user : users) {
            MentorDTO mentorDTO = modelMapper.map(user, MentorDTO.class);

            // Check if mentorDTO is not null
            if (mentorDTO != null) {
                Long mentorId = mentorDTO.getId();

                // Calculate average mentor rating for the current user
                Double averageMentorRating = feedbackRepository.calculateAverageRatingForMentorId(mentorId);
                mentorDTO.setAverageMentorRating(averageMentorRating);

                // Calculate average mentee rating for the current user
                Long menteeId = mentorDTO.getId(); // Assuming you have a menteeId property in MentorDTO

                // Check if menteeId is not null
                if (menteeId != null) {
                    Double averageMenteeRating = feedbackRepository.calculateAverageRatingForMenteeId(menteeId);
                    mentorDTO.setAverageMenteeRating(averageMenteeRating);
                }

                mentorDTOs.add(mentorDTO);
            }
        }
        return mentorDTOs;
    }


    @Override
    public List<MentorDTO> getSortedMentors(
            String expertise,
            String industry,
            String location,
            Long menteeId
    ) {
        logger.info("Getting sorted mentors with expertise: {}, industry: {}, location: {}, and mentee ID: {}", expertise, industry, location, menteeId);

        List<MentorDTO> mentorDTOList = getUsersByRole(UserRole.MENTOR);

        mentorDTOList = filterMentorsByConnectionStatus(mentorDTOList, menteeId);
        mentorDTOList = sortMentors(mentorDTOList, expertise, industry, location);
        calculateAverageRatings(mentorDTOList); // Remove the assignment, as it's the same list reference

        return mentorDTOList;
    }


    public List<MentorDTO> filterMentorsByConnectionStatus(List<MentorDTO> mentors, Long menteeId) {
        return mentors.stream()
                .filter(mentor -> mentorIsNotConnected(mentor, menteeId))
                .collect(Collectors.toList());
    }

    public List<MentorDTO> sortMentors(List<MentorDTO> mentors, String expertise, String industry, String location) {
        return mentors.stream()
                .sorted((mentor1, mentor2) -> {
                    int comparisonResult = 0;

                    if (expertise != null) {
                        comparisonResult = compareExpertise(mentor1.getExpertise(), mentor2.getExpertise(), expertise);
                    }

                    if (comparisonResult == 0 && industry != null) {
                        comparisonResult = compareValues(mentor1.getIndustry(), mentor2.getIndustry());
                    }

                    if (comparisonResult == 0 && location != null) {
                        comparisonResult = compareValues(mentor1.getLocation(), mentor2.getLocation());
                    }

                    if (comparisonResult == 0) {
                        comparisonResult = compareUserNames(mentor1.getUserName(), mentor2.getUserName());
                    }

                    return comparisonResult;
                })
                .collect(Collectors.toList());
    }

    private int compareUserNames(String userName1, String userName2) {
        if (userName1 == null && userName2 == null) {
            return 0; // Both userName values are null, consider them equal
        } else if (userName1 == null) {
            return 1; // userName1 is null, consider it greater
        } else if (userName2 == null) {
            return -1; // userName2 is null, consider it greater
        }

        // Continue with the comparison
        return userName1.compareToIgnoreCase(userName2);
    }

    public List<MentorDTO> calculateAverageRatings(List<MentorDTO> mentors) {
        for (MentorDTO mentorDTO : mentors) {
            Double averageMentorRating = feedbackRepository.calculateAverageRatingForMentorId(mentorDTO.getId());
            mentorDTO.setAverageMentorRating(averageMentorRating);
        }
        return mentors;
    }

    public int compareExpertise(String expertise1, String expertise2, String targetExpertise) {
        List<String> skills1 = Arrays.asList(expertise1.split(","));
        List<String> skills2 = Arrays.asList(expertise2.split(","));
        // Check if any of the skills match the target expertise
        boolean hasTargetSkill1 = skills1.stream().map(String::trim).anyMatch(skill -> skill.equalsIgnoreCase(targetExpertise));
        boolean hasTargetSkill2 = skills2.stream().map(String::trim).anyMatch(skill -> skill.equalsIgnoreCase(targetExpertise));
        if (hasTargetSkill1 && !hasTargetSkill2) {
            return -1; // mentor1 has the target expertise but mentor2 doesn't, so mentor1 comes before mentor2
        } else if (!hasTargetSkill1 && hasTargetSkill2) {
            return 1; // mentor1 doesn't have the target expertise but mentor2 does, so mentor1 comes after mentor2
        } else {
            return 0; // Both mentors either have the target expertise or don't have it, so the order remains unchanged
        }
    }

    public int compareValues(String value1, String value2) {
        if (value1.equalsIgnoreCase(value2)) {
            return 0;
        } else {
            return value1.compareToIgnoreCase(value2);
        }
    }

    public boolean mentorIsNotConnected(MentorDTO mentor, Long menteeId) {
        ConnectionRequestStatus[] connectedStatuses = {ConnectionRequestStatus.PENDING, ConnectionRequestStatus.ACCEPTED};
        User mentorUser = new User();

        mentorUser.setId(mentor.getId());
        mentorUser.setRoles(mentor.getRoles());
        mentorUser.setUserName(mentor.getUserName());
        mentorUser.setUserMail(mentor.getUserMail());
        mentorUser.setPhoneNumber(mentor.getPhoneNumber());
        mentorUser.setExpertise(mentor.getExpertise());
        mentorUser.setLocation(mentor.getLocation());
        mentorUser.setBio(mentor.getBio());
        mentorUser.setProfileImage(mentor.getProfileImage());
        mentorUser.setIndustry(mentor.getIndustry());
        mentorUser.setChargePerHour(mentor.getChargePerHour());
        // Set other properties of the mentorUser as needed

        boolean isConnected = connectionRequestRepository.existsByMentorAndMenteeIdAndStatusIn(mentorUser, menteeId, connectedStatuses);
        boolean isNotConnected = !isConnected;
        logger.info("Checking mentor connection status: Mentor ID - {}, Mentee ID - {}, Is Connected - {}", mentor.getId(), menteeId, isNotConnected);
        return isNotConnected;
    }


}
package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.dto.MenteeDTO;
import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.interfaces.IProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Autowired
    private final UserRepository userRepository;

    @Override
    public String uploadImage(String userMail, String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Optional<User> userOptional = Optional.ofNullable(userRepository.findByUserMail(userMail));
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setProfileImage(imageBytes);
                userRepository.save(user);
                logger.info("Image uploaded successfully for user with email: {}", userMail);
                return "Image uploaded successfully";
            }
        } catch (IllegalArgumentException e) {
            // Handle invalid Base64 string or other exceptions as needed
            logger.error("Error while uploading image for user with email: {}", userMail, e);
            throw new IllegalArgumentException("Invalid Base64 image data");
        }
        logger.error("Something went wrong while uploading image for user with email: {}", userMail);
        return "Something went wrong";
    }

    @Override
    public void updateMenteeProfile(MenteeDTO menteedto) {
        User user = getUserByMail(menteedto.getUserMail());
        user.setBio(menteedto.getBio());
        user.setMentoringRequiredFor(menteedto.getMentoringRequiredFor());
        user.setRoles(menteedto.getRoles());
        user.setLocation(menteedto.getLocation());
        user.setUserName(menteedto.getUserName());
        user.setPhoneNumber(menteedto.getPhoneNumber());
        updateUser(user);
        logger.info("Updated Mentee profile for user with email: {}", menteedto.getUserMail());
    }

    @Override
    public void updateMentorProfile(MentorDTO mentordto) {
        User user = getUserByMail(mentordto.getUserMail());
        user.setBio(mentordto.getBio());
        user.setExpertise(mentordto.getExpertise());
        user.setIndustry(mentordto.getIndustry());
        user.setRoles(mentordto.getRoles());
        user.setPhoneNumber(mentordto.getPhoneNumber());
        user.setLocation(mentordto.getLocation());
        user.setChargePerHour(mentordto.getChargePerHour());
        user.setUserName(mentordto.getUserName());
        updateUser(user);
        logger.info("Updated Mentor profile for user with email: {}", mentordto.getUserMail());
    }

    public User updateUser(User user) {
        User updatedUser = userRepository.save(user);
        logger.info("Updated user profile for user with ID: {}", updatedUser.getId());
        return updatedUser;
    }


    public User getUserByMail(String userMail) {
        User user = userRepository.findByUserMail(userMail);
        if (user == null) {
            logger.warn("User not found with email: {}", userMail);

        }
        logger.info("Retrieved user by email: {}", userMail);

        return user;
    }
}
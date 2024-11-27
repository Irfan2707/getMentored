package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.dto.MenteeDTO;
import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.entity.Picture;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.ProfileServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {


    private final ProfileServiceImpl profileServiceImpl;
    private final Logger logger = LoggerFactory.getLogger(ProfileController.class);


    /**
     * Endpoint to update Mentee Profile.
     *
     * @param menteedto The MenteeDTO containing the updated Mentee profile details.
     * @return A response entity indicating the result of the Mentee profile update process.
     */
    @PutMapping("/mentee")
    @ApiOperation(value = "Update Mentee Profile", notes = "Updates the profile of a Mentee")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid MenteeDTO menteedto) {
        profileServiceImpl.updateMenteeProfile(menteedto);
        logger.info("Mentee profile updated successfully.");
        return ResponseHandler.success("Mentee profile updated successfully.", HttpStatus.OK, "Mentee profile updated");
    }


    /**
     * Endpoint to update Mentor Profile.
     *
     * @param mentordto The MentorDTO containing the updated Mentor profile details.
     * @return A response entity indicating the result of the Mentor profile update process.
     */
    @PutMapping("/mentor")
    @ApiOperation(value = "Update Mentor Profile", notes = "Updates the profile of a Mentor")
    public ResponseEntity<Object> updateUser(@RequestBody MentorDTO mentordto) {
        profileServiceImpl.updateMentorProfile(mentordto);
        logger.info("Mentor profile updated successfully.");
        return ResponseHandler.success("Mentor profile updated successfully.", HttpStatus.OK, "Mentor profile updated");
    }


    /**
     * Endpoint to upload Profile Image.
     *
     * @param userMail The email of the user for whom the profile image is uploaded.
     * @param request  The Picture object containing the profile image data.
     * @return A response entity indicating the result of the profile image upload process.
     */

    @PutMapping("/profileImage")
    @ApiOperation("Upload Profile Image to your profile")
    public ResponseEntity<Object> uploadImage(
            @RequestParam String userMail,
            @RequestBody Picture request
    ) {
        String resultMessage = profileServiceImpl.uploadImage(userMail, request.getProfileImage());

        if (resultMessage == null) {
            logger.error("Failed to upload profile image for user: {}", userMail);
            return ResponseHandler.error("Failed to upload profile image.", HttpStatus.INTERNAL_SERVER_ERROR);

        } else {
            logger.info("Profile image uploaded successfully for user: {}", userMail);
            return ResponseHandler.success("Profile image uploaded successfully.", HttpStatus.OK, resultMessage);
        }
    }

}

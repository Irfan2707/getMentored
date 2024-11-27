package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.dto.MentorDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.SearchForMentorServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    @Autowired
    private final SearchForMentorServiceImpl searchForMentorService;
    private final Logger logger = LoggerFactory.getLogger(SearchController.class);
    public static final String USER_RETRIEVED_SUCCESSFULLY = "Users retrieved successfully.";


    /**
     * Endpoint to get users by their role.
     *
     * @param role The UserRole to search for.
     * @return A response entity containing a list of MentorDTOs based on the specified role.
     */
    @GetMapping("/getByRole")
    @ApiOperation("Searching users based on their Role")
    public ResponseEntity<Object> getUsersByRole(@RequestParam UserRole role) {
        List<MentorDTO> usersByRole = searchForMentorService.getUsersByRole(role);

        if (usersByRole == null) {
            logger.error("No users found for role: {}", role);
            return ResponseHandler.error("No users found for the specified role.", HttpStatus.NOT_FOUND);

        } else {
            logger.info("Users retrieved successfully by role: {}", role);
            return ResponseHandler.success(USER_RETRIEVED_SUCCESSFULLY, HttpStatus.OK, usersByRole);
        }
    }


    /**
     * Endpoint to search users by name, expertise, or industry.
     *
     * @param name      The name to search for.
     * @param expertise The expertise to search for.
     * @param industry  The industry to search for.
     * @return A response entity containing a list of users based on the specified search criteria.
     */
    @GetMapping("/searchByNameOrExpertiseOrIndustry")
    @ApiOperation("Searching users using their Name, Expertise, or Industry")
    public ResponseEntity<Object> getByNameOrExpertiseOrIndustry(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "expertise", required = false) String expertise,
            @RequestParam(value = "industry", required = false) String industry) {
        List<User> users = searchForMentorService.getUserByNameOrExpertiseOrIndustry(name, expertise, industry);

        if (users == null) {
            logger.error("No users found for the specified search criteria");
            return ResponseHandler.error("No users found for the specified search criteria.", HttpStatus.NOT_FOUND);

        } else {
            logger.info("Users retrieved successfully by name, expertise, or industry");
            return ResponseHandler.success(USER_RETRIEVED_SUCCESSFULLY, HttpStatus.OK, users);
        }
    }


    /**
     * Endpoint to search users by keyword (expertise or industry).
     *
     * @param keyword The keyword to search for.
     * @return A response entity containing a list of MentorDTOs based on the specified keyword.
     */
    @GetMapping("/searchByKeyword")
    @ApiOperation("Search users by expertise or industry")
    public ResponseEntity<Object> searchByKeyword(@RequestParam("keyword") String keyword) {
        List<MentorDTO> mentors = searchForMentorService.getByMentorDto(keyword);

        if (mentors == null) {
            logger.error("No users found for the specified keyword");
            return ResponseHandler.error("No users found for the specified keyword.", HttpStatus.NOT_FOUND);
        } else {
            logger.info("Users retrieved successfully by expertise or industry keyword");
            return ResponseHandler.success(USER_RETRIEVED_SUCCESSFULLY, HttpStatus.OK, mentors);

        }
    }


    /**
     * Endpoint to get sorted mentors by expertise, industry, and location.
     *
     * @param expertise The expertise to filter mentors.
     * @param industry  The industry to filter mentors.
     * @param location  The location to filter mentors.
     * @param menteeId  The ID of the mentee for personalized sorting.
     * @return A response entity containing a list of sorted MentorDTOs based on the specified criteria.
     */
    @GetMapping("/getSortedMentors")
    @ApiOperation("Sorting mentors by expertise, industry, and location")
    public ResponseEntity<Object> getSortedMentors(
            @RequestParam(required = false) String expertise,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String location,
            @RequestParam Long menteeId
    ) {
        List<MentorDTO> mentors = searchForMentorService.getSortedMentors(expertise, industry, location, menteeId);

        if (mentors == null) {
            logger.error("No mentors found for the specified criteria");
            return ResponseHandler.error("No mentors found for the specified criteria.", HttpStatus.NOT_FOUND);
        } else {
            logger.info("Mentors sorted successfully by expertise, industry, and location");
            return ResponseHandler.success("Mentors sorted successfully.", HttpStatus.OK, mentors);

        }
    }

}

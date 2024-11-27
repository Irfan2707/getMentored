package com.nineleaps.authentication.jwt.repository;


import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<User, Long> {
    //finding user by name
    List<User> findByuserNameContainingIgnoreCase(String name);

    //finding user by expertise
    List<User> findByExpertiseContaining(String expertise);

    //    //finding user by industry
    List<User> findByIndustryContainingIgnoreCase(String industry);

    //finding user by name or expertise or industry
    List<User> findByuserNameOrExpertiseOrIndustry(String name, String expertise, String industry);
    //finding user by role


    //---------------------------------------------------
    List<User> findByuserNameAndExpertiseContainingIgnoreCase(String name, String expertise);

    List<User> findByuserNameAndIndustryContaining(String name, String industry);

    List<User> findByuserNameAndIndustryAndExpertiseContaining(String name, String industry, String expertise);


    //Searching user by role(MENTEE,MENTOR)
    List<User> findByRoles(UserRole role);

    //Query used for searching using User class
    @Query("SELECT u FROM User u WHERE u.expertise LIKE %:keyword% OR u.industry LIKE %:keyword% OR u.userName LIKE %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);

    //Query used for searching using MENTORDTO
    @Query("SELECT u FROM User u WHERE u.expertise LIKE %:keyword% OR u.industry LIKE %:keyword% OR u.userName LIKE %:keyword%")
    List<User> findByMentorDto(@Param("keyword") String keyword);

}

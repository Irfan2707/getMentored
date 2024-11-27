package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.dto.SSOUserDto;
import com.nineleaps.authentication.jwt.dto.UserStatsDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhoneNumber(String phoneNumber);

    Optional<User> findById(Long id);

    User findByUserMail(String email);


    public List<User> findByExpertiseContaining(String keyword);

    User save(SSOUserDto newSsoUser);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT DISTINCT cr.mentor.id FROM ConnectionRequest cr WHERE cr.mentee.id = :menteeId AND cr.status IN ('ACCEPTED', 'PENDING'))")
    List<User> findUsersWithoutAcceptedConnections(@Param("menteeId") Long menteeId);


    @Query("SELECT COUNT(u) FROM User u WHERE :role MEMBER OF u.roles")
    long countUsersByRole(@Param("role") UserRole role);

    //	________________________________________________
//	Counting no of total users/mentess/mentors
    @Query("SELECT new com.nineleaps.authentication.jwt.dto.UserStatsDTO(" +
            "COUNT(u), " +
            "(SELECT COUNT(ur) FROM User u JOIN u.roles ur WHERE ur = 'MENTEE'), " +
            "(SELECT COUNT(ur) FROM User u JOIN u.roles ur WHERE ur = 'MENTOR')" +
            ") FROM User u")
    UserStatsDTO getRegisteredUsersCount();
//	_____________________________________________________-

    @Query("select u from User u where u.userMail=:email")
    public User getUserEmail(@Param("email") String email);

}

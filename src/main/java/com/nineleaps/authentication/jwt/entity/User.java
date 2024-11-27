package com.nineleaps.authentication.jwt.entity;


import com.nineleaps.authentication.jwt.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String userName;
    @Email
    @Column(name = "user_mail")
    private String userMail;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "user_password")
    private String userPassword;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @Column(name = "profile_image")
    @Lob
    private byte[] profileImage;

    @Column(name = "expertise")
    private String expertise;

    @Column(name = "location")
    private String location;

    @Column(name = "bio")
    private String bio;

    @Column(name = "industry")
    private String industry;

    @Column(name = "mentoring_required_for")
    private String mentoringRequiredFor;

    @Column(name = "charge_per_hour")
    private double chargePerHour;


    public User(Long id) {
        this.id = id;
    }


    public User(String userName, String userMail, Set<UserRole> roles) {
        this.userMail = userMail;
        this.roles = roles;
        this.userName = userName;
    }

    public User(String userMail, Set<UserRole> roles) {
        super();
        this.userMail = userMail;
        this.roles = roles;
    }


}

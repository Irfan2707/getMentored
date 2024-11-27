package com.nineleaps.authentication.jwt.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Getter
@Setter

@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public RefreshToken() {
        // This constructor intentionally left empty. No additional validation logic is required.
    }

    private RefreshToken(int id, String token, Instant expiryDate, User user) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public static RefreshTokenBuilder builder() {
        return new RefreshTokenBuilder();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class RefreshTokenBuilder {
        private int id;
        private String token;
        private Instant expiryDate;
        private User user;

        public RefreshTokenBuilder() {
            // This constructor intentionally left empty. No additional validation logic is required.
        }

        public RefreshTokenBuilder id(int id) {
            this.id = id;
            return this;
        }

        public RefreshTokenBuilder token(String token) {
            this.token = token;
            return this;
        }

        public RefreshTokenBuilder expiryDate(Instant expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public RefreshTokenBuilder user(User user) {
            this.user = user;
            return this;
        }

        public RefreshToken build() {
            return new RefreshToken(id, token, expiryDate, user);
        }
    }
}



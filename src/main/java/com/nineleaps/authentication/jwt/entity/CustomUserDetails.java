package com.nineleaps.authentication.jwt.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {


    private Long userId;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
    }


    public CustomUserDetails(String userMail, List<GrantedAuthority> authorities) {
        super(userMail, userMail, buildAuthoritiesSet(authorities));
    }


    private static Set<GrantedAuthority> buildAuthoritiesSet(List<GrantedAuthority> authorities) {
        return new HashSet<>(authorities);
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomUserDetails that = (CustomUserDetails) o;

        return userId == null ? that.userId == null : userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

}

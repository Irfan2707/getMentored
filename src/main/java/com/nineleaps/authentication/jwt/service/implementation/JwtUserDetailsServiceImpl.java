package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.entity.CustomUserDetails;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(JwtUserDetailsServiceImpl.class);

    private List<GrantedAuthority> getAuthorities(Set<UserRole> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String userMail) throws UsernameNotFoundException {
        User user = userRepository.findByUserMail(userMail);
        if (user == null) {
            log.error("User not found with email: {}", userMail);
            throw new UsernameNotFoundException("User not found with email: " + userMail);
        }

        CustomUserDetails userDetails;
        if (user.getUserPassword() != null && !user.getUserPassword().isEmpty()) {
            userDetails = new CustomUserDetails(
                    user.getUserMail(),
                    user.getUserPassword(),
                    getAuthorities(user.getRoles())
            );
        } else {
            userDetails = new CustomUserDetails(
                    user.getUserMail(),
                    getAuthorities(user.getRoles())
            );
        }

        userDetails.setUserId(user.getId());
        log.info("User ID set to: {}", userDetails.getUserId());
        return userDetails;
    }
}

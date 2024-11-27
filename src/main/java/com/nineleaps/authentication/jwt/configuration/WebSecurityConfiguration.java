package com.nineleaps.authentication.jwt.configuration;

import com.nineleaps.authentication.jwt.service.implementation.JwtUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true)
@SuppressWarnings("deprecation")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService jwtService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String mentee = "MENTEE";
        String mentor = "MENTOR";

        httpSecurity.cors();
        httpSecurity.csrf().disable();

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeRequests().antMatchers("/api/v1/signup/**", "/api/v1/profileuser", "/api/v1/otp", "/api/v1/verifyOTP/signup").permitAll();

        httpSecurity.authorizeRequests().antMatchers(
                "/api/v1/bookings/deletebyid/**", "/api/v1/bookings/createBookingggg**", "/api/v1/bookings/updatebyid", "/api/v1/bookings/findbymenteeid/**").hasAuthority(mentee);

        httpSecurity.authorizeRequests().antMatchers("/api/v1/checklistitems/",

                "/api/v1/checklistitems/deleteById", "/api/v1/checklistitems/updateById", "/api/v1/bookings/deletebyid/**", "/api/v1/search/searchbykeyword/**").hasAnyAuthority(mentee, mentor);

        httpSecurity.authorizeRequests().antMatchers("/user/").hasAnyAuthority(mentee, mentor);

        httpSecurity.authorizeRequests().antMatchers("/api/v1/connection-requests/accept/**").hasAuthority(mentor);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(jwtService).passwordEncoder(passwordEncoder());
    }
}

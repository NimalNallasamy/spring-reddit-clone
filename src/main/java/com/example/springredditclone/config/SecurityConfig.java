package com.example.springredditclone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class holds the complete security configuration for our backend!
 * */

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    /**
     * Disabling the CORS since we will be using JWT to authorize.
     * CORS attacks are done using Cookies which holds the Session.
     * Since we are using Rest APIs, which dont have any state, we don't need to worry on CORS.
     *
     * So any api of pattern /api/auth/** can be sent to the backend. Since these are the format for Rest Apis.
     * Other Urls should be authenticated.
     * */

    /**
     * Find the use of the methods, authorizeRequests, antMatchers, permitAll, anyRequest, authenticated.
     * */
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors()
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/api/auth/**")
        .permitAll()
        .anyRequest()
        .authenticated();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

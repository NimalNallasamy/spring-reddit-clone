package com.example.springredditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * This class acts as the data provider to authenticate the user.
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String userName;
    private String authenticationToken;
    private String refreshToken;
    private Instant expiresAt;

}

package com.example.springredditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class acts as the data provider to authenticate the user.
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String userName;
    private String authenticationToken;

}

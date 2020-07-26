package com.example.springredditclone.service;

import com.example.springredditclone.entity.RefreshToken;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Data
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid Refresh Token"));
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }

}

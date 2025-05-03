package com.compedia.services;

import com.compedia.entities.RefreshToken;

import com.compedia.entities.UserEntity;
import com.compedia.exceptions.NotFoundException;
import com.compedia.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public RefreshToken saveRefreshToken(RefreshToken refreshToken){
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findRefreshTokenByUser(Long userId){
        UserEntity user = userService.getUserById(userId);
        return refreshTokenRepository.findByUser(user).orElseThrow();
    }

    public void deleteRefreshTokenByUser(Long id){
        UserEntity user = userService.getUserById(id);
        refreshTokenRepository.deleteByUser(user);
    }

    public Optional<RefreshToken> findByToken(String refreshToken){
        return refreshTokenRepository.findByToken(refreshToken);
    }

    public void deleteRefreshTokenByToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }


}

package com.compedia.services;

import com.compedia.entities.RefreshToken;
import com.compedia.entities.UserEntity;
import com.compedia.exceptions.NotFoundException;
import com.compedia.repositories.RefreshTokenRepository;
import com.compedia.repositories.UserRepository;
import com.compedia.security.JwtService;
import com.compedia.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

//    @Value("${spring.security.jwt.refresh-token-validity}")
//    private final String refreshTokenValidity;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    public RefreshToken createRefreshToken(Long userId){
        // user
        UserEntity user = userRepository.findById(userId) // user
                .orElseThrow(()->new NotFoundException("User Not Found"));
        UserDetails userDetails = new UserDetailsImpl(user);
        String refreshToken = jwtService.generateRefreshToken(userDetails); // refresh token


        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(jwtService.extractExpiration(refreshToken))
                .build();

        return refreshTokenRepository.save(token);
    }


    public RefreshToken verifyExpriationRefreshToken(RefreshToken refreshToken){
        if(refreshToken.getExpiryDate().before(new Date())){
            throw new RuntimeException("Token Expired!");
        }
        return refreshToken;
    }

    public RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Refresh token is not found with token: "+token));
    }


//    public int deleteRefreshTokenByUserId(Long id){
//        UserEntity user = userRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("User Not Found"));
//        return refreshTokenRepository.deleteByUser(user);
//    }

    public void deleteByToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }





}

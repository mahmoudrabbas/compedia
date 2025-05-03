package com.compedia.security;

import com.compedia.DTOs.*;
import com.compedia.entities.RefreshToken;
import com.compedia.services.RefreshTokenService;
import com.compedia.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    private final UserDetailsServiceImpl userDetailsService;

    public LoginResponse register(@Valid UserRequest dto){
        return userService.signUp(dto);
    }

    public LoginResponse login(LoginRequest dto){
        return userService.signIn(dto);
    }

//    public ResponseEntity<LoginResponse> refresh(@Valid RefreshTokenRequest request){
//        String refreshToken = request.getRefreshToken();
//        String username = jwtService.extractUsername(refreshToken);
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        if(jwtService.isTokenValid(refreshToken, userDetails)){
//            String accessToken = jwtService.generateAccessToken(userDetails);
//            return ResponseEntity.ok().body(new LoginResponse(accessToken, refreshToken));
//        }
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }


    public ResponseEntity<LoginResponse> refreshToken(RefreshToken refreshToken){
        String token = refreshToken.getToken();
        UserDetails userDetails = new UserDetailsImpl(refreshToken.getUser());
        if(jwtService.isTokenValid(token, userDetails)){
            String accessToken = jwtService.generateAccessToken(userDetails);
            return ResponseEntity.ok().body(new LoginResponse(accessToken, token));
        }
        refreshTokenService.deleteByToken(token);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public String logout(RefreshToken refreshToken){
        String token = refreshToken.getToken();
        refreshTokenService.deleteByToken(token);
        return "Logout Successfully..";
    }

}

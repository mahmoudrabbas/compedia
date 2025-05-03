package com.compedia.security;

import com.compedia.DTOs.*;
import com.compedia.entities.RefreshToken;
import com.compedia.entities.RoleEntity;
import com.compedia.entities.UserEntity;
import com.compedia.enums.RoleName;
import com.compedia.exceptions.AlreadyExistsException;
import com.compedia.repositories.UserRepository;
import com.compedia.services.RefreshTokenService;
import com.compedia.services.RoleService;
import com.compedia.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    public LoginResponse signUp(UserRequest entity){
        // checking if email or username exist
        userRepository.findByEmail(entity.getEmail())
                .ifPresent((err) -> {throw new AlreadyExistsException("Email Is Already In Use");});
        userRepository.findByUsername(entity.getUsername())
                .ifPresent((err) -> {throw new AlreadyExistsException("Username Is Already In Use");});

        // convert userRequest dto to user entity
        UserEntity user = new UserEntity().mapToEntity(entity);

        // get the user role and add it to user entity
        RoleEntity userRole = roleService.getRoleByName(RoleName.ROLE_USER);
        user.getRoles().add(userRole);

        // encode user entity password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // save the user entity
        UserEntity userEntity = userRepository.save(user);


        String refreshToken = jwtService.generateRefreshToken(new UserDetailsImpl(userEntity));
        String accessToken = jwtService.generateAccessToken(new UserDetailsImpl(userEntity));

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse signIn(@Valid LoginRequest credentials){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userService.getByUsername(userDetails.getUsername());

        if(refreshTokenService.findRefreshTokenByUser(user.getId())!=null){
            if(!jwtService.isTokenExpired(refreshTokenService.findRefreshTokenByUser(user.getId()).getToken())){
                throw new AlreadyExistsException("You are Already Signed In");
            }else {
                refreshTokenService.deleteRefreshTokenByUser(user.getId());
                throw new AlreadyExistsException("You are Signed Out");
            }
        }

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new LoginResponse(accessToken, refreshToken);

    }


    public ResponseEntity<LoginResponse> refreshAccessToken(String authRefreshToken){

        if(refreshTokenService.findByToken(authRefreshToken).isPresent()){
            RefreshToken refreshToken = refreshTokenService.findByToken(authRefreshToken).get();
            if(jwtService.isTokenExpired(refreshToken.getToken())){
                refreshTokenService.deleteRefreshTokenByToken(authRefreshToken);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }else {
                String accessToken = jwtService.generateAccessToken(new UserDetailsImpl(refreshToken.getUser()));
                return ResponseEntity.ok().body(new LoginResponse(accessToken, refreshToken.getToken()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public String logout(String refreshToken){
        if(refreshTokenService.findByToken(refreshToken).isPresent()){
            refreshTokenService.deleteRefreshTokenByToken(refreshToken);
            return "Logout Successfully..";
        }
        throw new AlreadyExistsException("You Already Signed Out");
    }

}

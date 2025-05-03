package com.compedia.services;

import com.compedia.DTOs.LoginRequest;
import com.compedia.DTOs.LoginResponse;
import com.compedia.DTOs.UserRequest;
import com.compedia.DTOs.UserResponse;
import com.compedia.entities.RoleEntity;
import com.compedia.entities.UserEntity;
import com.compedia.enums.Gender;
import com.compedia.enums.RoleName;
import com.compedia.exceptions.AlreadyExistsException;
import com.compedia.exceptions.NotFoundException;
import com.compedia.repositories.UserRepository;
import com.compedia.security.JwtService;
import com.compedia.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    // get all users
    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    // get user by id
    public UserEntity getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found with id: "+id));
    }

    // add user
    public LoginResponse signUp(UserRequest entity){

        userRepository.findByEmail(entity.getEmail())
                .ifPresent((err) -> {throw new AlreadyExistsException("Email Is Already In Use");});

        userRepository.findByUsername(entity.getUsername())
                .ifPresent((err) -> {throw new AlreadyExistsException("Username Is Already In Use");});

        UserEntity user = new UserEntity().mapToEntity(entity);

        RoleEntity userRole = roleService.getRoleByName(RoleName.ROLE_USER);
        user.getRoles().add(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity userEntity = userRepository.save(user);

        String refreshToken = refreshTokenService.createRefreshToken(entity.getId()).getToken();
        String accessToken = jwtService.generateAccessToken(new UserDetailsImpl(userEntity));

        return new LoginResponse(accessToken, refreshToken);
    }

    // update user
    public UserResponse updateUser(UserRequest entity){
        UserEntity user = userRepository.findById(entity.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: "+entity.getId()));


        userRepository.findByEmail(entity.getEmail())
                .filter(existed -> !existed.getId().equals(entity.getId()))
                .ifPresent((err) -> {throw new AlreadyExistsException("Email Is Already In Use");});

        userRepository.findByUsername(entity.getUsername())
                .filter(existed -> !existed.getId().equals(entity.getId()))
                .ifPresent((err) -> {throw new AlreadyExistsException("Username Is Already In Use");});

        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());
        user.setUsername(entity.getUsername());
        user.setGender(entity.getGender().equalsIgnoreCase("Male")? Gender.MALE:Gender.FEMALE);

        if(!entity.getPassword().isBlank()){
            user.setPassword(passwordEncoder.encode(entity.getPassword()));
        }

        UserEntity userEntity = userRepository.save(user);

        return new UserResponse().mapToDto(userEntity);
    }


    // delete user
    public int deleteUserById(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not Found To Delete With Id: "+id));
        userRepository.delete(user);
        return 1;
    }

    // get users by RoleName
    public List<UserEntity> getUsersByRoleName(RoleName roleName){
        return userRepository.findUsersByRoleName(roleName);
    }

    public LoginResponse signIn(@Valid LoginRequest credentials){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        String accessToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new LoginResponse(accessToken, refreshToken);
    }

}

package com.compedia.services;

import com.compedia.DTOs.UserRequest;
import com.compedia.DTOs.UserResponse;

import com.compedia.entities.UserEntity;
import com.compedia.enums.Gender;
import com.compedia.enums.RoleName;
import com.compedia.exceptions.AlreadyExistsException;
import com.compedia.exceptions.NotFoundException;
import com.compedia.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // get all users
    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    // get user by id
    public UserEntity getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found with id: "+id));
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


    // get user by username
    public UserEntity getByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }


}

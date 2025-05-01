package com.compedia.services;

import com.compedia.entities.UserEntity;
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

    // add user
    public UserEntity addUser(UserEntity entity){

        userRepository.findByEmail(entity.getEmail())
                .ifPresent((err) -> {throw new AlreadyExistsException("Email Is Already In Use");});

        userRepository.findByUsername(entity.getUsername())
                .ifPresent((err) -> {throw new AlreadyExistsException("Username Is Already In Use");});

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userRepository.save(entity);
    }

    // update user
    public UserEntity updateUser(UserEntity entity){

        userRepository.findByEmail(entity.getEmail())
                .ifPresent((err) -> {throw new AlreadyExistsException("Email Is Already In Use");});

        userRepository.findByUsername(entity.getUsername())
                .filter(existed -> !existed.getId().equals(entity.getId()))
                .ifPresent((err) -> {throw new AlreadyExistsException("Username Is Already In Use");});

        UserEntity user = userRepository.findById(entity.getId())
                .filter(existed -> !existed.getId().equals(entity.getId()))
                .orElseThrow(() -> new NotFoundException("User not found with id: "+entity.getId()));

        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());
        user.setUsername(entity.getUsername());
        user.setGender(entity.getGender());

        if(!entity.getPassword().isBlank()){
            user.setPassword(passwordEncoder.encode(entity.getPassword()));
        }

        return userRepository.save(user);
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
}

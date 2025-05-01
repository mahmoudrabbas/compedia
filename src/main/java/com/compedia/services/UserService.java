package com.compedia.services;

import com.compedia.entities.UserEntity;
import com.compedia.exceptions.NotFoundException;
import com.compedia.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    // get all users
    public List<UserEntity> getAll(){
        return userRepository.findAll();
    }

    // get user by id
    public UserEntity getById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found with id: "+id));
    }

//    // add user
//    public UserEntity addUser()
}

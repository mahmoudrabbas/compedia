package com.compedia.controllers;

import com.compedia.DTOs.UserRequest;
import com.compedia.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserBy(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody @Valid UserRequest entity){
        return ResponseEntity.ok().body(userService.signUp(entity));
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserRequest entity){
        return ResponseEntity.ok().body(userService.updateUser(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.deleteUserById(id));
    }
}

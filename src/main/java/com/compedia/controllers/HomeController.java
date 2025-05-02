package com.compedia.controllers;

import com.compedia.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> home(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

}

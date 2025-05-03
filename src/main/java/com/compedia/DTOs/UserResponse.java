package com.compedia.DTOs;

import com.compedia.entities.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String gender;
    private LocalDateTime createdAt;
    private Set<String> roles;


    public UserResponse mapToDto(UserEntity user){
        return builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .gender(user.getGender().name())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.toSet()))
                .build();
    }
}

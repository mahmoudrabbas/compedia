package com.compedia.DTOs;

import com.compedia.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    @NotNull(message = "First Name Is Required")
    private String firstName;
    @NotNull(message = "Last Name Is Required")
    private String lastName;
    @NotNull(message = "Enter A Valid Username")
    private String username;
    @Email(message = "Enter A valid Email!")
    private String email;
    @NotNull(message = "Password Is Required")
    private String password;
    @NotNull
    private String gender;
}

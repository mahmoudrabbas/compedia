package com.compedia.DTOs;

import com.compedia.enums.Gender;
import com.compedia.utils.SanitizingDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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

    private Long id;
    @NotNull(message = "First Name Is Required")
    @JsonDeserialize(using = SanitizingDeserializer.class)
    private String firstName;
    @JsonDeserialize(using = SanitizingDeserializer.class)
    @NotNull(message = "Last Name Is Required")
    private String lastName;
    @JsonDeserialize(using = SanitizingDeserializer.class)
    @NotNull(message = "Enter A Valid Username")
    private String username;
    @Email(message = "Enter A valid Email!")
    private String email;
    @NotNull(message = "Password Is Required")
    private String password;
    @NotNull
    @JsonDeserialize(using = SanitizingDeserializer.class)
    private String gender;
}

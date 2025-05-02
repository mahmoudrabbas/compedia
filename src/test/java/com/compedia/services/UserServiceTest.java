package com.compedia.services;

import com.compedia.entities.UserEntity;
import com.compedia.enums.Gender;
import com.compedia.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        user = new UserEntity();
        user.setId(1L);
        user.setGender(Gender.MALE);
        user.setFirstName("Mahmoud");
        user.setLastName("Ramadan");
        user.setEmail("mra@gmail.com");
        user.setUsername("mra553");
        user.setPassword("mra5553");
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserEntity> list = userService.getAllUsers();

        assertEquals(1, list.size());
        assertEquals("Mahmoud", list.get(0).getFirstName());
    }

    @Test
    void getUserById() {
    }

    @Test
    void addUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUserById() {
    }

    @Test
    void getUsersByRoleName() {
    }
}
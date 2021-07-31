package com.hu.Virtualize.services.login;

import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.UserEntity;
import com.hu.Virtualize.repositories.AdminRepository;
import com.hu.Virtualize.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class UsersServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersServiceImpl usersService;

    private UserEntity userEntity;

    private AdminEntity adminEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity(1L,"Praveen","praveentripathi236@gmail.com","123","Banglore",null);
        adminEntity = new AdminEntity(1L,"Praveen","praveentripathi236@gmail.com","123",null);
    }

    @Test
    void addUserWhichAlreadyExist(){
        given(userRepository.findByUserEmail(anyString())).willReturn(userEntity);

        assertThrows(ResponseStatusException.class, () -> usersService.addUser(userEntity));
    }

    @Test
    void addAdminWhichAlreadyExist(){
        given(adminRepository.findByAdminEmail(anyString())).willReturn(Optional.of(adminEntity));

        assertThrows(ResponseStatusException.class, () -> usersService.addAdmin(adminEntity));
    }
}
package com.hu.Virtualize.services.login;

import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.repositories.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private AdminEntity adminEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminEntity = new AdminEntity(1L,"Praveen","pravtripathi@deloite.com","123",null);
    }

    @Test
    void getAdminById() {
        given(adminRepository.findById(anyLong())).willReturn(Optional.of(adminEntity));

        adminService.getAdminById(adminEntity.getAdminId());

    }

    @Test
    void adminNotPresent(){
        given(adminRepository.findById(1L)).willReturn(Optional.of(adminEntity));
        assertThrows(ResponseStatusException.class, () -> adminService.getAdminById(anyLong()));
    }
}
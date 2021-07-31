package com.hu.Virtualize.services.login;

import com.hu.Virtualize.commands.login.LoginCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.UserEntity;
import com.hu.Virtualize.geolocation.GeoLocation;
import com.hu.Virtualize.repositories.AdminRepository;
import com.hu.Virtualize.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginServiceImplTest {

    @Mock private BCryptPasswordEncoder passwordEncoder;

    @Mock private UserRepository userRepository;

    @Mock private AdminRepository adminRepository;

     private GeoLocation geoLocation;

     private LoginServiceImpl loginService;

    private LoginCommand loginUser, loginAdmin;
    private UserEntity userEntity;
    private AdminEntity adminEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        geoLocation = new GeoLocation();
        loginService = new LoginServiceImpl(userRepository,adminRepository,passwordEncoder, geoLocation);
        loginUser = new LoginCommand("zs@deloitte.com","123","USER", 28.1,75.1);
        loginAdmin = new LoginCommand("sai@deloitte.com","123","ADMIN");

        userEntity = new UserEntity(1L,"zs","zs@deloitte.com","123","Dadri",null);
        adminEntity = new AdminEntity(1L,"saif","sai@deloitte.com","123",null);
    }

    @Test
    void loginWithUser() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(userEntity);
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);
//        when(geoLocation.findCityByLatAndLng(anyDouble(), anyDouble())).thenReturn("Delhi");
        when(userRepository.save(any())).thenReturn(userEntity);

        UserEntity userEntity1 = (UserEntity) loginService.login(loginUser);

        assertEquals(userEntity1.getUserId(), userEntity.getUserId());
    }

    @Test
    void loginWithUserNotPresent() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(userEntity);
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(false);

        UserEntity userEntity1 = (UserEntity) loginService.login(loginUser);

        assertNull(userEntity1);
    }

    @Test
    void loginWithAdmin() {
        when(adminRepository.findByAdminEmail(anyString())).thenReturn(Optional.of(adminEntity));
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);

        AdminEntity adminEntity1 = (AdminEntity) loginService.login(loginAdmin);

        assertEquals(adminEntity1.getAdminId(), adminEntity.getAdminId());
    }

    @Test
    void loginWithAdminNotPresent() {
        when(adminRepository.findByAdminEmail(anyString())).thenReturn(Optional.of(adminEntity));
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(false);

        AdminEntity adminEntity1 = (AdminEntity) loginService.login(loginAdmin);

        assertNull(adminEntity1);
    }

    @Test
    void validEmailUser() {
        when(userRepository.findByUserEmail(any())).thenReturn(userEntity);

        boolean status = loginService.validEmail(loginUser);

        assertTrue(status);
    }

    @Test
    void validEmailAdmin() {
        when(adminRepository.findByAdminEmail(any())).thenReturn(Optional.of(adminEntity));

        boolean status = loginService.validEmail(loginAdmin);

        assertTrue(status);
    }

    @Test
    void updatePasswordUser() {
        when(passwordEncoder.encode(any())).thenReturn("123");
        when(userRepository.findByUserEmail(anyString())).thenReturn(userEntity);

        UserEntity userEntity1 = (UserEntity) loginService.updatePassword(loginUser);

        verify(userRepository, times(1)).findByUserEmail(anyString());
    }

    @Test
    void updatePasswordAdmin() {
        when(passwordEncoder.encode(any())).thenReturn("123");
        when(adminRepository.findByAdminEmail(anyString())).thenReturn(Optional.of(adminEntity));

        AdminEntity adminEntity1 = (AdminEntity) loginService.updatePassword(loginAdmin);

        verify(adminRepository, times(1)).findByAdminEmail(anyString());
    }
}
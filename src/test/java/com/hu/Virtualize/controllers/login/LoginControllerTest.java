package com.hu.Virtualize.controllers.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hu.Virtualize.commands.login.EmailRequest;
import com.hu.Virtualize.commands.login.LoginCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.UserEntity;
import com.hu.Virtualize.services.login.AdminServiceImpl;
import com.hu.Virtualize.services.login.SendMailImpl;
import com.hu.Virtualize.services.login.LoginServiceImpl;
import com.hu.Virtualize.services.login.SendMailImpl;
import com.hu.Virtualize.services.login.UsersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SendMailImpl sendMail;

    @MockBean
    private UsersServiceImpl usersService;

    @MockBean
    private LoginServiceImpl loginService;

    @MockBean
    private AdminServiceImpl adminService;

    private UserEntity userEntity;
    private LoginCommand loginCommand;
    private AdminEntity adminEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity(1L, "zhatab","zsaifi@gmail.com","123","dadri",null);
        loginCommand = new LoginCommand("zsaifi@gmail.com","123","USER");
        adminEntity = new AdminEntity(1L,"abc","abc@deloitte.com","123",null);
    }

    @Test
    void login() throws Exception{
        when(loginService.login(loginCommand)).thenReturn(userEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand)))
                .andExpect(status().isOk())
                .andReturn();

        UserEntity userEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),UserEntity.class);

        assertEquals(userEntity1.getUserId(), userEntity.getUserId());
    }

    @Test
    void loginFailed() throws Exception{

        when(loginService.login(loginCommand)).thenReturn(null);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void addUser() throws Exception{
        when(usersService.addUser(any())).thenReturn(userEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEntity)))
                .andExpect(status().isOk())
                .andReturn();

        verify(usersService,times(1)).addUser(any());
    }

    @Test
    void addAdmin() throws Exception{
        when(usersService.addAdmin(any())).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/admin/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminEntity)))
                .andExpect(status().isOk())
                .andReturn();

        verify(usersService,times(1)).addAdmin(any());
    }

    @Test
    void sendEmail() throws Exception{
        EmailRequest emailRequest = new EmailRequest("zs@gmail.com","abcd","hahahaha");

        when(sendMail.sendEmail(anyString(),anyString(),anyString())).thenReturn(true);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/sendemail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void sendEmailFailed() throws Exception{
        EmailRequest emailRequest = new EmailRequest("zs@gmail.com","abcd","hahahaha");

        when(sendMail.sendEmail(anyString(),anyString(),anyString())).thenReturn(false);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/sendemail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void getAdminById() throws Exception{
        when(adminService.getAdminById(anyLong())).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/admin/{id}",1))
                .andExpect(status().isOk())
                .andReturn();

        verify(adminService,times(1)).getAdminById(anyLong());
    }

    @Test
    void validEmail() throws Exception{
        when(loginService.validEmail(any())).thenReturn(true);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/validEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void validEmailNotAvailable() throws Exception{
        when(loginService.validEmail(loginCommand)).thenReturn(false);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/validEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void updatePassword() throws Exception{
        when(loginService.updatePassword(any())).thenReturn(userEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void updatePasswordWithNull() throws Exception{
        when(loginService.updatePassword(any())).thenReturn(null);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand)))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
package com.hu.Virtualize.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hu.Virtualize.commands.user.UserInterestCommand;
import com.hu.Virtualize.entities.RecommendEntity;
import com.hu.Virtualize.entities.UserEntity;
import com.hu.Virtualize.services.user.UserInterestServiceImpl;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserInterestController.class)
class UserInterestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserInterestServiceImpl userInterestService;

    private UserInterestCommand userInterestCommand;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userInterestCommand = new UserInterestCommand(1L, 1L, "Peter baba");
        userEntity = new UserEntity(1L, "zsaifi","zs@deloitte.com","123","dadri",null);
    }

    @Test
    void insertInterest() throws Exception{
        when(userInterestService.insertInterest(any())).thenReturn(userEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/interest/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInterestCommand)))
                .andExpect(status().isOk())
                .andReturn();

        UserEntity userEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),UserEntity.class);

        assertEquals(userEntity.getUserId(), userEntity1.getUserId());
    }

    @Test
    void deleteInterest() throws Exception{
        when(userInterestService.deleteInterest(any())).thenReturn(userEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.delete("/interest/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInterestCommand)))
                .andExpect(status().isOk())
                .andReturn();

        UserEntity userEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),UserEntity.class);

        assertEquals(userEntity.getUserId(), userEntity1.getUserId());
    }
}
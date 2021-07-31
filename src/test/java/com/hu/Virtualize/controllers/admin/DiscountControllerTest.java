package com.hu.Virtualize.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hu.Virtualize.commands.admin.DiscountCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.services.admin.service.DiscountService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(DiscountController.class)
class DiscountControllerTest {

    @MockBean
    DiscountService discountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    AdminEntity adminEntity;
    DiscountCommand discountCommand ;

    @BeforeEach
    void setUp() {
        adminEntity = new AdminEntity("zsaifi","zsaifi@deloitte.com","123");

        discountCommand = new DiscountCommand(1L,1L,1L,0L,"Eid offer",10,"2021-07-23","xyz....");
    }

    @Test
    void insertDiscount() throws Exception {
        when(discountService.insertDiscount(discountCommand)).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/admin/discount/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(discountCommand)))
                .andExpect(status().isOk())
                .andReturn();

        AdminEntity adminEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),AdminEntity.class);

        assertEquals(adminEntity1.getAdminId(), adminEntity.getAdminId());
    }

    @Test
    void deleteDiscount() throws Exception{
        when(discountService.deleteDiscount(discountCommand)).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.delete("/admin/discount/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(discountCommand)))
                .andExpect(status().isOk())
                .andReturn();

        AdminEntity adminEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),AdminEntity.class);

        assertEquals(adminEntity1.getAdminId(), adminEntity.getAdminId());

    }
}
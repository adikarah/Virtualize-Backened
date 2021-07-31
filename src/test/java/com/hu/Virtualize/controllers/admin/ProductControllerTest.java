package com.hu.Virtualize.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hu.Virtualize.commands.admin.ProductCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.services.admin.ProductCreateServiceImpl;
import com.hu.Virtualize.services.admin.ShopServiceImpl;
import com.hu.Virtualize.services.admin.service.ProductCreateService;
import com.hu.Virtualize.services.admin.service.ShopService;
import com.hu.Virtualize.services.user.ProductServiceImpl;
import org.checkerframework.checker.units.qual.A;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductCreateServiceImpl productCreateService;

    @MockBean
    private ShopServiceImpl shopService;

    private ProductCommand productCommand;
    private AdminEntity adminEntity;

    @BeforeEach
    void setUp() {
        productCommand = new ProductCommand(1L,1L,1L,"abc",200,"Peter","ncjdn","CLOTHE","size",null);
        adminEntity = new AdminEntity(1L,"User","u@gmail.com","123",null);
    }

    @Test
    void insertShop() throws Exception{
        when(productCreateService.insertProduct(productCommand)).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/admin/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCommand)))
                .andExpect(status().isOk())
                .andReturn();

        AdminEntity adminEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),AdminEntity.class);

        assertEquals(adminEntity1.getAdminId(), adminEntity.getAdminId());
    }

    @Test
    void updateProduct() throws Exception{
        when(productCreateService.updateProduct(any())).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCommand)))
                .andExpect(status().isOk())
                .andReturn();

        AdminEntity adminEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),AdminEntity.class);

        assertEquals(adminEntity1.getAdminId(), adminEntity.getAdminId());
    }

    @Test
    void deleteProduct() throws Exception{
        when(productCreateService.deleteProduct(productCommand)).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.delete("/admin/product/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCommand)))
                .andExpect(status().isOk())
                .andReturn();

        AdminEntity adminEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),AdminEntity.class);

        assertEquals(adminEntity1.getAdminId(), adminEntity.getAdminId());
    }

    @Test
    void getAllProductType() throws Exception{

        when(productCreateService.getAllProductType()).thenReturn(Arrays.asList("abc","efg"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/admin/product/types"))
                .andExpect(status().isOk())
                .andReturn();

        List<String> products =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertEquals(products.size(), 2);
 }
}
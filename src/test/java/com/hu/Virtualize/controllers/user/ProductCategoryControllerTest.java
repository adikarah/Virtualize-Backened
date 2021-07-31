package com.hu.Virtualize.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hu.Virtualize.services.user.ProductCategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductCategoryController.class)
class ProductCategoryControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductCategoryServiceImpl productCategoryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getStoreName() throws Exception{
        when(productCategoryService.getStoreName(anyString())).thenReturn(Arrays.asList("hello","hii"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/productCategory/stores/{productType}", "h"))
                .andExpect(status().isOk())
                .andReturn();

        List<String> products =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertEquals(products.size(), 2);
    }

    @Test
    void getProductNames() throws Exception{
        when(productCategoryService.getProductNames(anyString())).thenReturn(Arrays.asList("hello","hii"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/productCategory/products/{productType}", "h"))
                .andExpect(status().isOk())
                .andReturn();

        List<String> products =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertEquals(products.size(), 2);
    }
}
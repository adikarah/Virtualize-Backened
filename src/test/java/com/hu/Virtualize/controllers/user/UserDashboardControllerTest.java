package com.hu.Virtualize.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hu.Virtualize.controllers.admin.ProductController;
import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.services.user.ProductServiceImpl;
import com.hu.Virtualize.services.user.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserDashboardController.class)
class UserDashboardControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getProduct() throws Exception{
        when(productService.getProduct()).thenReturn(Arrays.asList(new ProductEntity(), new ProductEntity()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(status().isOk())
                .andReturn();

        List<String> products =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertEquals(products.size(), 2);
    }

    @Test
    void testGetProduct() throws Exception{
        when(productService.getProduct(anyString())).thenReturn(Arrays.asList(new ProductEntity(), new ProductEntity()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/product/{category}", "CLOTHE"))
                .andExpect(status().isOk())
                .andReturn();

        List<String> products =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertEquals(products.size(), 2);
    }

    @Test
    void insertProductImage() throws Exception{
        MockMultipartFile multipartFile = new MockMultipartFile("image", "testing.txt", "text/plain", "zsaifi".getBytes());

        when(productService.insertProductImage(anyLong(),any())).thenReturn("Done");

        mockMvc.perform(multipart("/product/insertImage/{productId}", 1)
                .file(multipartFile))
                .andExpect(status().isOk());

        verify(productService,times(1)).insertProductImage(anyLong(),any());
    }

    @Test
    void renderImageFromDB() throws Exception{
        byte[] image = "zhatab".getBytes();
        Byte[] productImage = new Byte[image.length];
        int i=0;
        for(byte x : image) {
            productImage[i++] = x;
        }
        ProductEntity productEntity = new ProductEntity(1L,"abc",100,"abc","CLOTHE","MAle","abcdefghi",productImage,null);

        when(productService.findProductById(anyLong())).thenReturn(productEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/product/image/{productId}", 1))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void renderImageFromDBWithException() throws Exception{

        when(productService.findProductById(anyLong())).thenReturn(null);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/product/image/{productId}", 1))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }
}
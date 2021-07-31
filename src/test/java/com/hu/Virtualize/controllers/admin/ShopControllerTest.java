package com.hu.Virtualize.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hu.Virtualize.commands.admin.ShopCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.ShopEntity;
import com.hu.Virtualize.services.admin.ShopServiceImpl;
import com.hu.Virtualize.services.admin.service.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ShopController.class)
class ShopControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopServiceImpl shopService;

    private ShopCommand shopCommand;
    private AdminEntity adminEntity;

    @BeforeEach
    void setUp() {
        shopCommand = new ShopCommand(1L,1L, "abc","dadri","ascdc","29AAACC1206D2ZB");
        adminEntity = new AdminEntity(1L,"User","u@gmail.com","123",null);
    }

    @Test
    void insertShop() throws Exception{
        when(shopService.insertShop(shopCommand)).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/admin/shop/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shopCommand)))
                .andExpect(status().isOk())
                .andReturn();

        AdminEntity adminEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),AdminEntity.class);

        assertEquals(adminEntity1.getAdminId(), adminEntity.getAdminId());
    }

    @Test
    void updateShop() throws Exception{
        when(shopService.updateShop(shopCommand)).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.put("/admin/shop/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shopCommand)))
                .andExpect(status().isOk())
                .andReturn();

        AdminEntity adminEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),AdminEntity.class);

        assertEquals(adminEntity1.getAdminId(), adminEntity.getAdminId());
    }

    @Test
    void deleteShop() throws Exception{
        when(shopService.deleteShop(shopCommand)).thenReturn(adminEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.delete("/admin/shop/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shopCommand)))
                .andExpect(status().isOk())
                .andReturn();

        AdminEntity adminEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),AdminEntity.class);

        assertEquals(adminEntity1.getAdminId(), adminEntity.getAdminId());
    }

    @Test
    void getShopsById() throws Exception{
        Set<ShopEntity> shops = new HashSet<>(Arrays.asList(new ShopEntity(), new ShopEntity()));

        when(shopService.getAllShopsByAdminId(anyLong())).thenReturn(shops);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/admin/shop/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();

        Set resultShop = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),Set.class);
        assertEquals(resultShop.size(),1);
    }

    @Test
    void insertShopImage() throws Exception{
        MockMultipartFile multipartFile = new MockMultipartFile("image", "testing.txt", "text/plain", "zsaifi".getBytes());

        when(shopService.insertShopImage(anyLong(),any())).thenReturn("Done");

        mockMvc.perform(multipart("/admin/shop/insertImage/{shopId}", 1)
                .file(multipartFile))
                .andExpect(status().isOk());

        verify(shopService,times(1)).insertShopImage(anyLong(),any());
    }

    @Test
    void renderImageFromDB() throws Exception{
        byte[] image = "zhatab".getBytes();
        Byte[] shopImage = new Byte[image.length];
        int i=0;
        for(byte x : image) {
            shopImage[i++] = x;
        }

        ShopEntity shopEntity = new ShopEntity(1L,"abc","abcd","abcded","29AAACC1206D2ZB",shopImage,null);

        when(shopService.findShopById(anyLong())).thenReturn(shopEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/admin/shop/image/{shopId}", 1))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void renderImageFromDBWithException() throws Exception{
        ShopEntity shopEntity = new ShopEntity(1L,"abc","abcd","abcded","29AAACC1206D2ZB",null,null);

        when(shopService.findShopById(anyLong())).thenReturn(shopEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/admin/shop/image/{shopId}", 1))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
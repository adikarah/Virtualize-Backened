package com.hu.Virtualize.controllers.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hu.Virtualize.commands.home.RecommendCommand;
import com.hu.Virtualize.controllers.admin.ShopController;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.RecommendEntity;
import com.hu.Virtualize.entities.ShopEntity;
import com.hu.Virtualize.services.home.RecommendServiceImpl;
import com.hu.Virtualize.services.home.service.RecommendService;
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

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RecommendController.class)
class RecommendControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendServiceImpl recommendService;

    private RecommendCommand recommendCommand;
    private RecommendEntity recommendEntity;

    @BeforeEach
    void setUp() {
        recommendCommand = new RecommendCommand(1L, "CLOTHE", "abcd","2021-07-12");

        byte[] image = "zhatab".getBytes();
        Byte[] recommendImage = new Byte[image.length];
        int i=0;
        for(byte x : image) {
            recommendImage[i++] = x;
        }
        recommendEntity = new RecommendEntity(1L, recommendImage,"CLOTHE","abcd", Date.valueOf("2021-07-12"));
    }

    @Test
    void insertRecommend() throws Exception{
        when(recommendService.insertRecommend(recommendCommand)).thenReturn(recommendEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/recommend/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendCommand)))
                .andExpect(status().isOk())
                .andReturn();

        RecommendEntity recommendEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),RecommendEntity.class);

        assertEquals(recommendEntity1.getRecommendId(), recommendEntity.getRecommendId());

    }

    @Test
    void insertRecommendImage() throws Exception{
        MockMultipartFile multipartFile = new MockMultipartFile("image", "testing.txt", "text/plain", "zsaifi".getBytes());

        when(recommendService.insertRecommendImage(anyLong(),any())).thenReturn("Done");

        mockMvc.perform(multipart("/recommend/insertImage/{recommendId}", 1)
                .file(multipartFile))
                .andExpect(status().isOk());

        verify(recommendService,times(1)).insertRecommendImage(anyLong(),any());

    }

    @Test
    void deleteRecommend() throws Exception{
        when(recommendService.deleteRecommend(anyLong())).thenReturn("Done");

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.delete("/recommend/delete/{recommendId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendCommand)))
                .andExpect(status().isOk())
                .andReturn();

        String status =  mvcResult.getResponse().getContentAsString();

        assertEquals(status,"Done");
    }

    @Test
    void findRecommendById() throws Exception{
        when(recommendService.findById(anyLong())).thenReturn(recommendEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/recommend/showRecommend/{recommendId}",1))
                .andExpect(status().isOk())
                .andReturn();

        RecommendEntity recommendEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),RecommendEntity.class);

        assertEquals(recommendEntity1.getRecommendId(), recommendEntity1.getRecommendId());
    }

    @Test
    void findShowRecommendId() throws Exception{
        when(recommendService.findShowRecommends()).thenReturn(Arrays.asList(recommendEntity));

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/recommend/showRecommend"))
                .andExpect(status().isOk())
                .andReturn();

        List<RecommendEntity> recommendEntity1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),List.class);

        assertEquals(recommendEntity1.size(), 1);

    }

    @Test
    void renderImageFromDB() throws Exception{
        when(recommendService.findById(anyLong())).thenReturn(recommendEntity);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/recommend/bar/{recommendId}", 1))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void renderImageFromDBWithException() throws Exception{

        when(recommendService.findById(anyLong())).thenReturn(null);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/recommend/bar/{recommendId}", 1))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
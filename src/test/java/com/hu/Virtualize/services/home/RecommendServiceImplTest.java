package com.hu.Virtualize.services.home;

import com.hu.Virtualize.commands.home.RecommendCommand;
import com.hu.Virtualize.entities.RecommendEntity;
import com.hu.Virtualize.repositories.RecommendRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class RecommendServiceImplTest {

    @Mock
    private RecommendRepository recommendRepository;

    @InjectMocks
    private RecommendServiceImpl recommendService;

    private RecommendCommand recommendCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recommendCommand = new RecommendCommand(1L,"Clothes","Best fashionable products with 15% discount","2021-06-23");

    }

    @Test
    void insertRecommend() {
        RecommendEntity recommendEntity = new RecommendEntity();
        recommendEntity = recommendService.insertRecommend(recommendCommand);

    }

    @Test
    void insertRecommendImage() {
    }

    @Test
    void deleteRecommend() {
        recommendService.deleteRecommend(recommendCommand.getRecommendId());
    }

    @Test
    void findById() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("image", "testing.txt", "text/plain", "Praveen".getBytes());
        byte[] image = multipartFile.getBytes();
        Byte[] recommendImage = new Byte[image.length];
        int i=0;
        for(byte x : image) {
            recommendImage[i++] = x;
        }
        RecommendEntity recommendEntity = new RecommendEntity(1l,recommendImage,"Kitchens","Best Kitechen Products with exciting discounts", Date.valueOf("2021-06-21"));
        given(recommendRepository.findById(anyLong())).willReturn(Optional.of(recommendEntity));
        recommendService.findById(recommendCommand.getRecommendId());

    }

    @Test
    void IdNotFound(){
        given(recommendRepository.findById(anyLong())).willReturn(null);
        RecommendCommand recommendCommand1 = new RecommendCommand();
        assertThrows(ResponseStatusException.class, ()-> recommendService.findById(recommendCommand1.getRecommendId()));
    }

    @Test
    void findShowRecommends() throws IOException {
        List<RecommendEntity> recommendations = new ArrayList<>();

        MockMultipartFile multipartFile1 = new MockMultipartFile("image", "testing.txt", "text/plain", "Praveen".getBytes());
        MockMultipartFile multipartFile2 = new MockMultipartFile("image", "testing.txt", "text/plain", "Zhatab".getBytes());

        byte[] image1 = multipartFile1.getBytes();
        Byte[] recommendImage1 = new Byte[image1.length];
        int i=0;
        for(byte x : image1) {
            recommendImage1[i++] = x;
        }

        byte[] image2 = multipartFile2.getBytes();
        Byte[] recommendImage2 = new Byte[image2.length];
        int j=0;
        for(byte x : image2) {
            recommendImage2[j++] = x;
        }

        recommendations.add(new RecommendEntity(1L,recommendImage1,"Clothes","Best deal",Date.valueOf("2021-06-24")));
        recommendations.add(new RecommendEntity(2L,recommendImage2,"Restaurant","Best offers",Date.valueOf("2021-06-23")));

        given(recommendRepository.findAll()).willReturn(recommendations);
        recommendations = recommendService.findShowRecommends();
    }
}
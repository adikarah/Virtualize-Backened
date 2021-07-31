package com.hu.Virtualize.services.home.service;

import com.hu.Virtualize.commands.home.RecommendCommand;
import com.hu.Virtualize.entities.RecommendEntity;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

public interface RecommendService {
    RecommendEntity insertRecommend(RecommendCommand recommendCommand);
    RecommendEntity findById(Long recommendId);
    List<RecommendEntity> findShowRecommends();
    String insertRecommendImage(Long recommendId, MultipartFile multipartFile);
    String deleteRecommend(Long recommendId);
}

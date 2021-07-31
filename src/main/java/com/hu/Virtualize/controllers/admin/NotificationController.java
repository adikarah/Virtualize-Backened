package com.hu.Virtualize.controllers.admin;

import com.hu.Virtualize.controllers.home.RecommendController;
import com.hu.Virtualize.entities.RecommendEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private RecommendController recommendController;

    @MessageMapping("/notification")
    @SendTo("/topic/received")
    public RecommendEntity greeting(String recommendId)  {
        log.info("Send this recommendation to notification bar");
        ResponseEntity<?> responseEntity = recommendController.findRecommendById(recommendId);
        return (RecommendEntity) responseEntity.getBody();
    }
}

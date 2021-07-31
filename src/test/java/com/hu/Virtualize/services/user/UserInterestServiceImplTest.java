package com.hu.Virtualize.services.user;

import com.hu.Virtualize.commands.user.UserInterestCommand;
import com.hu.Virtualize.entities.UserEntity;
import com.hu.Virtualize.entities.UserInterestEntity;
import com.hu.Virtualize.repositories.UserInterestRepository;
import com.hu.Virtualize.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class UserInterestServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInterestRepository userInterestRepository;

    @InjectMocks
    private UserInterestServiceImpl userInterestService;

    private UserInterestCommand userInterestCommand;

    private UserEntity userEntity;

    private UserInterestEntity userInterestEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userInterestCommand = new UserInterestCommand(1L,1L,"Restaurant");
        userEntity = new UserEntity(1L,"Praveen","p@gmail.com","123","Noida",null);
        userInterestEntity = new UserInterestEntity(1L,"Restaurant");
    }

    @Test
    void insertInterest() {
        UserInterestCommand userInterestCommand1 = new UserInterestCommand();
        given(userRepository.findById(userInterestCommand1.getUserId())).willReturn(Optional.of(userEntity));

        Set<UserInterestEntity> userEntities = new HashSet<>();
        userEntities.add(userInterestEntity);

        userEntity.setUserInterestEntities(userEntities);
        userEntity.getUserInterestEntities().add(new UserInterestEntity(2L,"Clothes"));
        userRepository.save(userEntity);
        userInterestService.insertInterest(userInterestCommand1);
    }

    @Test
    void insertInterestForInvalidUser(){
        given(userRepository.findById(anyLong())).willReturn(null);
        UserInterestCommand userInterestCommand1 = new UserInterestCommand();
        assertThrows(ResponseStatusException.class, () ->userInterestService.insertInterest(userInterestCommand1));
    }

    @Test
    void insertInterestWhichAlreadyPresent(){
        given(userRepository.findById(userInterestCommand.getUserId())).willReturn(Optional.of(userEntity));
        Set<UserInterestEntity> userEntities = new HashSet<>();
        userEntities.add(userInterestEntity);

        userEntity.setUserInterestEntities(userEntities);
        userEntity.getUserInterestEntities().add(new UserInterestEntity(2L,"Clothes"));
        userRepository.save(userEntity);
        assertThrows(ResponseStatusException.class, () ->userInterestService.insertInterest(userInterestCommand));
    }

    @Test
    void deleteInterest() {
        given(userRepository.findById(userInterestCommand.getUserId())).willReturn(Optional.of(userEntity));
        Set<UserInterestEntity> userEntities = new HashSet<>();
        userEntities.add(userInterestEntity);

        userEntity.setUserInterestEntities(userEntities);
        userEntity.getUserInterestEntities().add(new UserInterestEntity(2L,"Clothes"));
        userRepository.save(userEntity);
        userInterestService.deleteInterest(userInterestCommand);
    }

    @Test
    void deleteInterestForInvalidUser(){
        given(userRepository.findById(anyLong())).willReturn(null);
        UserInterestCommand userInterestCommand1 = new UserInterestCommand();
        assertThrows(ResponseStatusException.class, ()->userInterestService.deleteInterest(userInterestCommand1));
    }
}
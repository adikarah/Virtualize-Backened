package com.hu.Virtualize.services.user;

import com.hu.Virtualize.commands.user.UserInterestCommand;
import com.hu.Virtualize.entities.UserEntity;
import com.hu.Virtualize.entities.UserInterestEntity;
import com.hu.Virtualize.repositories.UserInterestRepository;
import com.hu.Virtualize.repositories.UserRepository;
import com.hu.Virtualize.services.user.service.UserInterestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserInterestServiceImpl implements UserInterestService {
    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private UserInterestRepository userInterestRepository;

    /**
     * This function will add new interest in user list.
     * If the interest is already present, then it will return exception.
     * @param userInterestCommand user interest details.
     * @return user entity details.
     */
    @Transactional
    @Override
    public UserEntity insertInterest(UserInterestCommand userInterestCommand) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userInterestCommand.getUserId());

        if(userEntityOptional.isEmpty()) {
            log.error("Invalid user");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user");
        }

        UserEntity userEntity = userEntityOptional.get();

        // check this interest is already available in the user interest list or not.
        userEntity.getUserInterestEntities().forEach(userInterestEntity -> {
            if(userInterestEntity.getInterest().equals(userInterestCommand.getInterest())) {
                log.error("This interest is already available in user interest list");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This interest is already available in user interest list");
            }
        });

        userEntity.getUserInterestEntities().add(new UserInterestEntity(userInterestCommand.getInterest()));
        userEntity = userRepository.save(userEntity);
        return userEntity;
    }

    /**
     * This function will delete the user interest in user list.
     * @param userInterestCommand user interest details.
     * @return updated user details.
     */
    @Transactional
    public  UserEntity deleteInterest(UserInterestCommand userInterestCommand) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userInterestCommand.getUserId());

        if(userEntityOptional.isEmpty()) {
            log.error("Invalid user");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user");
        }

        UserEntity userEntity = userEntityOptional.get();

        // it will store remain user interest
        Set<UserInterestEntity> userInterestEntitySet = new HashSet<>();

        // check the delete user interest available in user entity
        userEntity.getUserInterestEntities().forEach(userInterestEntity -> {
            if(!userInterestEntity.getUserInterestId().equals(userInterestCommand.getUserInterestId())) {
                userInterestEntitySet.add(userInterestEntity);
            }
        });

        // update user details.
        userEntity.setUserInterestEntities(userInterestEntitySet);
        userEntity = userRepository.save(userEntity);

        // delete user interest in database.
        userInterestRepository.deleteById(userInterestCommand.getUserInterestId());

        return userEntity;
    }
}

package com.hu.Virtualize.controllers.user;

import com.hu.Virtualize.commands.user.UserInterestCommand;
import com.hu.Virtualize.entities.UserEntity;
import com.hu.Virtualize.services.user.service.UserInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/interest")
@RestController
@CrossOrigin("*")
public class UserInterestController {
    @Autowired
    private UserInterestService userInterestService;

    /**
     * This function will add new interest in user list.
     * @param userInterestCommand user interest details.
     * @return User entity details.
     */
    @PostMapping("/create")
    public ResponseEntity<?> insertInterest(@RequestBody UserInterestCommand userInterestCommand) {
        UserEntity userEntity = userInterestService.insertInterest(userInterestCommand);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    /**
     * This function will delete the user interest in user list.
     * @param userInterestCommand user interest details.
     * @return updated user details.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInterest(@RequestBody UserInterestCommand userInterestCommand) {
        UserEntity userEntity = userInterestService.deleteInterest(userInterestCommand);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }
}

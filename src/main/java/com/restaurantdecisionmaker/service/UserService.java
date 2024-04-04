package com.restaurantdecisionmaker.service;

import com.restaurantdecisionmaker.exception.UserNotFoundException;
import com.restaurantdecisionmaker.model.User;
import com.restaurantdecisionmaker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserByName(String userName) {
        return userRepository.findByUsername(userName);
    }

    public User saveUser(User user) {
        final User existingUser = getUserByName(user.getUsername());
        if(null == existingUser) {
            return userRepository.save(user);
        }
        log.info("User already registered. UserName: {}", user.getUsername());
        throw new UserNotFoundException("User already registered.");
    }
}


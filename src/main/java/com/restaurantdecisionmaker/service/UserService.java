package com.restaurantdecisionmaker.service;

import com.restaurantdecisionmaker.exception.UserNotFoundException;
import com.restaurantdecisionmaker.model.User;
import com.restaurantdecisionmaker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
        throw new UserNotFoundException("User already registered.");
    }
}


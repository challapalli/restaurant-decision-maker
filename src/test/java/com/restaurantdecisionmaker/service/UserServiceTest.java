package com.restaurantdecisionmaker.service;

import com.restaurantdecisionmaker.exception.UserNotFoundException;
import com.restaurantdecisionmaker.model.User;
import com.restaurantdecisionmaker.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAllInBatch();
    }

    @Test
    public void testGetUserByName_ExistingUser() {
        String userName = "testUser";
        User mockUser = new User();
        mockUser.setUsername(userName);
        when(userRepository.findByUsername(userName)).thenReturn(mockUser);

        User retrievedUser = userService.getUserByName(userName);

        verify(userRepository, times(1)).findByUsername(userName);
        assertEquals(mockUser, retrievedUser);
    }

    @Test
    public void testGetUserByName_NonExistentUser() {
        String userName = "testUser";
        when(userRepository.findByUsername(userName)).thenReturn(null);

        User retrievedUser = userService.getUserByName(userName);

        verify(userRepository, times(1)).findByUsername(userName);
        assertNull(retrievedUser);
    }

    @Test
    public void testSaveUser_NewUser() {
        String userName = "testUser";
        User newUser = new User();
        newUser.setUsername(userName);
        when(userRepository.findByUsername(userName)).thenReturn(null);
        when(userRepository.save(newUser)).thenReturn(newUser);

        User savedUser = userService.saveUser(newUser);

        verify(userRepository, times(1)).findByUsername(userName);
        verify(userRepository, times(1)).save(newUser);
        assertEquals(newUser, savedUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void testSaveUser_ExistingUser() {
        String userName = "testUser";
        User existingUser = new User();
        existingUser.setUsername(userName);
        User newUser = new User();
        newUser.setUsername(userName);
        when(userRepository.findByUsername(userName)).thenReturn(existingUser);

        userService.saveUser(newUser);

        verify(userRepository, times(1)).findByUsername(userName);
        verify(userRepository, never()).save(newUser);
    }

}
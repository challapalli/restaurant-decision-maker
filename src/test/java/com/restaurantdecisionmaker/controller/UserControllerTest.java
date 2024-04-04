package com.restaurantdecisionmaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantdecisionmaker.model.User;
import com.restaurantdecisionmaker.service.JwtService;
import com.restaurantdecisionmaker.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void testRegisterUser_Success() throws Exception {
        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userService.saveUser(any())).thenReturn(getUser(username, password));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUser(username, password))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() throws Exception {
        String username = "existingUser";
        String password = "existingPassword";

        when(userService.saveUser(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUser(username, password))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAuthenticateAndGetToken_Success() throws Exception {
        String username = "testUser";
        String password = "testPassword";
        String token = "testToken";

        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken(username, password, List.of()));
        when(jwtService.generateToken(username)).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUser(username, password))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void testAuthenticateAndGetToken_InvalidCredentials() throws Exception {
        String username = "invalidUser";
        String password = "invalidPassword";

        when(authenticationManager.authenticate(any())).thenReturn(Mockito.mock(Authentication.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUser(username, password))))
                .andExpect(status().isForbidden());
    }

    User getUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
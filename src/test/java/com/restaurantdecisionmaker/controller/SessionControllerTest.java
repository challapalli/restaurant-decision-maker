package com.restaurantdecisionmaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.model.Session;
import com.restaurantdecisionmaker.service.SessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessionService sessionService;

    @Test
    @WithMockUser(username = "user")
    public void testGetAllSessions() throws Exception {
        Mockito.when(sessionService.getAllSessions()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "user")
    public void testCreateSession() throws Exception {
        String createdBy = "user1";
        Session session = new Session();
        session.setUsername(createdBy);

        Mockito.when(sessionService.saveSession(Mockito.any(Session.class))).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"createdBy\": \"" + createdBy + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(createdBy));
    }

    @Test
    @WithMockUser(username = "user")
    public void testJoinSession() throws Exception {
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);

        Mockito.when(sessionService.joinSession(sessionId)).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sessions/{sessionId}/join", sessionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sessionId));
    }

    @Test
    @WithMockUser(username = "user")
    public void testEndSession() throws Exception {
        Long sessionId = 1L;
        Restaurant restaurant = new Restaurant();

        Mockito.when(sessionService.endSession(sessionId)).thenReturn(restaurant);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sessions/{sessionId}/end", sessionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
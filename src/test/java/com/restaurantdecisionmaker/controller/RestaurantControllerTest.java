package com.restaurantdecisionmaker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantdecisionmaker.exception.SessionNotFoundException;
import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.service.RestaurantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerTest {

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user")
    public void testGetAllRestaurants_Success() throws Exception {
        Long sessionId = 1L;
        List<Restaurant> mockRestaurants = new ArrayList<>();
        when(restaurantService.getAllRestaurantsBySessionId(sessionId)).thenReturn(mockRestaurants);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants/{sessionId}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(restaurantService, times(1)).getAllRestaurantsBySessionId(sessionId);
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetAllRestaurants_NotFound() throws Exception {
        Long sessionId = 1L;
        when(restaurantService.getAllRestaurantsBySessionId(sessionId)).thenThrow(new SessionNotFoundException("SessionId not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants/{sessionId}", sessionId))
                .andExpect(status().isNotFound());

        verify(restaurantService, times(1)).getAllRestaurantsBySessionId(sessionId);
    }

    @Test
    @WithMockUser(username = "user")
    public void testCreateRestaurant_Success() throws Exception {
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("Test Restaurant");
        when(restaurantService.saveRestaurant(any())).thenReturn(newRestaurant);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newRestaurant)))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.name").value((newRestaurant.getName())));

        verify(restaurantService, times(1)).saveRestaurant(any());
    }

    @Test
    @WithMockUser(username = "user")
    public void testCreateRestaurant_NotFound() throws Exception {
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("Test Restaurant");
        when(restaurantService.saveRestaurant(newRestaurant)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newRestaurant)))
                .andExpect(status().isNotFound());

        verify(restaurantService, times(1)).saveRestaurant(any());
    }

    static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

}
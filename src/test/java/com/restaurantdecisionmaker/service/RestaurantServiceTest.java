package com.restaurantdecisionmaker.service;

import com.restaurantdecisionmaker.exception.SessionNotFoundException;
import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.model.Session;
import com.restaurantdecisionmaker.repository.RestaurantRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestaurantServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private WebSocketService webSocketService;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        restaurantRepository.deleteAll();
    }

    @Test
    public void testGetAllRestaurantsBySessionId_ValidSession() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setEnded(false);
        List<Restaurant> mockRestaurants = new ArrayList<>();

        when(sessionService.getSessionById(sessionId)).thenReturn(mockSession);
        when(restaurantRepository.findBySessionId(sessionId)).thenReturn(mockRestaurants);

        List<Restaurant> restaurants = restaurantService.getAllRestaurantsBySessionId(sessionId);

        verify(sessionService, times(1)).getSessionById(sessionId);
        verify(restaurantRepository, times(1)).findBySessionId(sessionId);
        assertEquals(mockRestaurants, restaurants);
    }

    @Test(expected = SessionNotFoundException.class)
    public void testGetAllRestaurantsBySessionId_InvalidSession() {
        Long sessionId = 1L;
        when(sessionService.getSessionById(sessionId)).thenReturn(null);

        restaurantService.getAllRestaurantsBySessionId(sessionId);

        verify(sessionService, times(1)).getSessionById(sessionId);
        verify(restaurantRepository, never()).findBySessionId(sessionId);
    }

    @Test
    public void testGetAllRestaurants() {
        List<Restaurant> mockRestaurants = new ArrayList<>();
        when(restaurantRepository.findAll()).thenReturn(mockRestaurants);

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        verify(restaurantRepository, times(1)).findAll();
        assertEquals(mockRestaurants, restaurants);
    }

    @Test
    public void testSaveRestaurant_ValidSession() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setEnded(false);
        Restaurant restaurant = new Restaurant();
        restaurant.setSessionId(sessionId);
        List<Restaurant> mockUpdatedRestaurants = new ArrayList<>();

        when(sessionService.getSessionById(sessionId)).thenReturn(mockSession);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantRepository.findBySessionId(sessionId)).thenReturn(mockUpdatedRestaurants);
        doNothing().when(webSocketService).sendRestaurantUpdate(mockUpdatedRestaurants);

        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);

        verify(sessionService, times(1)).getSessionById(sessionId);
        verify(restaurantRepository, times(1)).save(restaurant);
        verify(restaurantRepository, times(1)).findBySessionId(sessionId);
        verify(webSocketService, times(1)).sendRestaurantUpdate(mockUpdatedRestaurants);

        assertEquals(restaurant, savedRestaurant);
    }


    }
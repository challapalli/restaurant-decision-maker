package com.restaurantdecisionmaker.service;

import com.restaurantdecisionmaker.model.Restaurant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendRestaurantUpdate(List<Restaurant> restaurants) {
        messagingTemplate.convertAndSend("/topic/restaurants", restaurants);
        log.info("Invoked webclient....");
    }

    public void sendSelectedRestaurantUpdate(Restaurant restaurant) {
        messagingTemplate.convertAndSend("/topic/selectedRestaurants", restaurant);
        log.info("Invoked selectedRestaurants....{}", restaurant);
    }
}

package com.restaurantdecisionmaker.controller;

import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RestaurantWebSocketController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantWebSocketController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @MessageMapping("/updateRestaurants")
    @SendTo("/topic/restaurants")
    public List<Restaurant> updateRestaurants() {
        return restaurantService.getAllRestaurants();
    }
}
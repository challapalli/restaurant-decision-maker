package com.restaurantdecisionmaker.controller;

import com.restaurantdecisionmaker.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RestaurantWebSocketController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantWebSocketController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @MessageMapping("/updateRestaurants")
    @SendTo("/topic/restaurants")
    public String updateRestaurants() {
        //return restaurantService.getAllRestaurants();
        return "hello";
    }
}
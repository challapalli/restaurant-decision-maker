package com.restaurantdecisionmaker.controller;

import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{sessionId}")
    public List<Restaurant> getAllRestaurants(@PathVariable Long sessionId) {
        return restaurantService.getAllRestaurantsBySessionId(sessionId);
    }

    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        var data = restaurantService.saveRestaurant(restaurant);
        if(null == data) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SessionId not found.");
        }
        return data;
    }

}


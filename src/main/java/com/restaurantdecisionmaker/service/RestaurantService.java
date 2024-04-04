package com.restaurantdecisionmaker.service;

import com.restaurantdecisionmaker.exception.SessionNotFoundException;
import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.model.Session;
import com.restaurantdecisionmaker.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private WebSocketService webSocketService;

    public List<Restaurant> getAllRestaurantsBySessionId(Long sessionId) {
        Session session = sessionService.getSessionById(sessionId);
        if(!ObjectUtils.isEmpty(session) && !session.isEnded()) {
            return restaurantRepository.findBySessionId(sessionId);
        }
        throw new SessionNotFoundException("SessionId not found");
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        Session session = sessionService.getSessionById(restaurant.getSessionId());
        if(!ObjectUtils.isEmpty(session) && !session.isEnded()) {
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            log.info("Saved the Restaurant Data.");
            webSocketService.sendRestaurantUpdate(restaurantRepository.findBySessionId(restaurant.getSessionId()));
            return savedRestaurant;
        }
        throw new SessionNotFoundException("SessionId not found");
    }
}


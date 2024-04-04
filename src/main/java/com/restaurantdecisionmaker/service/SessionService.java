package com.restaurantdecisionmaker.service;

import com.restaurantdecisionmaker.exception.SessionNotFoundException;
import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.model.Session;
import com.restaurantdecisionmaker.repository.RestaurantRepository;
import com.restaurantdecisionmaker.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private WebSocketService webSocketService;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public Session getSessionById(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session != null) {
            return session;
        }
        log.info("SessionId doesn't exist. SessionId: {}", sessionId);
        throw new SessionNotFoundException("SessionId not found");
    }

    public Session joinSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session != null && !session.isEnded()) {
            return session;
        }
        log.info("SessionId doesn't exist. SessionId: {}", sessionId);
        throw new SessionNotFoundException("Session doesn't exist");
    }

    public Restaurant endSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session != null) {
            session.setEnded(true);
            sessionRepository.save(session);
            final List<Restaurant> restaurants = restaurantRepository.findBySessionId(sessionId);
            int randomIndex = new Random().nextInt(restaurants.size());
            var res = restaurants.get(randomIndex);
            webSocketService.sendSelectedRestaurantUpdate(res);
            return res;
        }
        log.info("SessionId doesn't exist. SessionId: {}", sessionId);
        throw new SessionNotFoundException("SessionId not found");
    }
}


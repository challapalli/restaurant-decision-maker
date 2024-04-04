package com.restaurantdecisionmaker.controller;

import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.model.Session;
import com.restaurantdecisionmaker.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin
@Slf4j
public class SessionController {
    public static final String CREATED_BY = "createdBy";
    @Autowired
    private SessionService sessionService;

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @PostMapping
    public Session createSession(@RequestBody Map<String, String> req) {
        var sessionCreatedBy = req.get(CREATED_BY);
        log.info("Session creation initiated by : {}", sessionCreatedBy);
        Session s = new Session();
        s.setEnded(false);
        s.setUsername(sessionCreatedBy);
        return sessionService.saveSession(s);
    }

    @PostMapping("/{sessionId}/join")
    public Session joinSession(@PathVariable Long sessionId) {
        return sessionService.joinSession(sessionId);
    }

    @PostMapping("/{sessionId}/end")
    public Restaurant endSession(@PathVariable Long sessionId) {
        return sessionService.endSession(sessionId);
    }
}


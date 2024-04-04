package com.restaurantdecisionmaker.controller;

import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.model.Session;
import com.restaurantdecisionmaker.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @PostMapping
    public Session createSession(@RequestBody Map<String, String> req) {
        Session s = new Session();
        s.setEnded(false);
        s.setUsername(req.get("createdBy"));
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


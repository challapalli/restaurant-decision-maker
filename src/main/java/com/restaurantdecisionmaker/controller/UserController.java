package com.restaurantdecisionmaker.controller;

import com.restaurantdecisionmaker.model.User;
import com.restaurantdecisionmaker.model.AuthResponse;
import com.restaurantdecisionmaker.service.JwtService;
import com.restaurantdecisionmaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> login(@RequestBody User request) {
        final String encode = passwordEncoder.encode(request.getPassword());
        request.setPassword(encode);
        var user = userService.saveUser(request);
        if (null != user) {
            var res = Map.of(
                    "status", "Success",
                    "message", "User registered successfully"
            );
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.badRequest().body("User already exists");
        }
    }

    @PostMapping("/login")
    public AuthResponse authenticateAndGetToken(@RequestBody User authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername());
            return new AuthResponse(authRequest.getUsername(), token);
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}

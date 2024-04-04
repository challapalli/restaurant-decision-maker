package com.restaurantdecisionmaker.controller;

import com.restaurantdecisionmaker.model.User;
import com.restaurantdecisionmaker.model.AuthResponse;
import com.restaurantdecisionmaker.service.JwtService;
import com.restaurantdecisionmaker.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("User register request received. UserName: {}", request.getUsername());
        final String encode = passwordEncoder.encode(request.getPassword());
        request.setPassword(encode);
        var user = userService.saveUser(request);
        if (null != user) {
            var res = Map.of(
                    "status", "Success",
                    "message", "User registered successfully"
            );
            log.info("User registered successfully. UserName: {}", request.getUsername());
            return ResponseEntity.ok(res);
        } else {
            log.info("User already exists.  UserName: {}", request.getUsername());
            return ResponseEntity.badRequest().body("User already exists");
        }
    }

    @PostMapping("/login")
    public AuthResponse authenticateAndGetToken(@RequestBody User authRequest) {
        log.info("User login initiated by User: {}", authRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername());
            log.info("User login successful. UserName: {}", authRequest.getUsername());
            return new AuthResponse(authRequest.getUsername(), token);
        } else {
            log.info("Invalid User Request for login. UserName: {}", authRequest.getUsername());
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}

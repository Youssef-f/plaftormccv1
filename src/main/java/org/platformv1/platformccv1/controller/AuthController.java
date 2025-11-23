package org.platformv1.platformccv1.controller;

import org.platformv1.platformccv1.dto.LoginRequest;
import org.platformv1.platformccv1.dto.LoginResponse;
import org.platformv1.platformccv1.dto.RegisterRequest;
import org.platformv1.platformccv1.dto.RegisterResponse;
import org.platformv1.platformccv1.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

}

package org.platformv1.platformccv1.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.platformv1.platformccv1.dto.*;
import org.platformv1.platformccv1.entity.Profile;
import org.platformv1.platformccv1.entity.User;
import org.platformv1.platformccv1.repository.ProfileRepository;
import org.platformv1.platformccv1.repository.UserRepository;
import org.platformv1.platformccv1.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService  {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ProfileRepository profileRepository;
    private final AuthenticationManager authenticationManager;


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public RegisterResponse register(RegisterRequest request) {

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already taken");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(savedUser);
        profile.setDisplayName(savedUser.getUsername());
        profile.setBio("");
        profile.setAvatarUrl(null);
        profile.setSkills("");
        profile.setLocation("");
        profile.setCreatedAt(LocalDateTime.now());

        profileRepository.save(profile);

        String token = jwtService.generateToken(savedUser);

        RegisterResponse response = new RegisterResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setToken(token);

        return response;
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }


    public ProfileResponse getLoggedUser(HttpServletRequest req) {

        String token = jwtService.extractToken(req);
        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        ProfileResponse res = new ProfileResponse();
        res.setId(user.getId());
        res.setDisplayName(profile.getDisplayName());
        res.setBio(profile.getBio());
        res.setAvatarUrl(profile.getAvatarUrl());
        res.setSkills(profile.getSkills());
        res.setLocation(profile.getLocation());
        res.setRole(user.getRole());
        System.out.println("Loaded user role = " + user.getRole());


        return res;
    }

}

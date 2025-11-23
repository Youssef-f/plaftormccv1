package org.platformv1.platformccv1.services;

import lombok.AllArgsConstructor;
import org.platformv1.platformccv1.dto.LoginRequest;
import org.platformv1.platformccv1.dto.LoginResponse;
import org.platformv1.platformccv1.dto.RegisterRequest;
import org.platformv1.platformccv1.dto.RegisterResponse;
import org.platformv1.platformccv1.entity.Profile;
import org.platformv1.platformccv1.entity.User;
import org.platformv1.platformccv1.repository.ProfileRepository;
import org.platformv1.platformccv1.repository.UserRepository;
import org.platformv1.platformccv1.security.JwtService;
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
        user.setRole("USER");
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


        RegisterResponse response = new RegisterResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());

        return response;
    }
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(token);
    }


}

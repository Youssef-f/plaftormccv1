package org.platformv1.platformccv1.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.ProfileResponse;
import org.platformv1.platformccv1.dto.ProfileUpdateRequest;
import org.platformv1.platformccv1.entity.Profile;
import org.platformv1.platformccv1.entity.User;
import org.platformv1.platformccv1.repository.ProfileRepository;
import org.platformv1.platformccv1.repository.UserRepository;
import org.platformv1.platformccv1.security.JwtService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public ProfileResponse getMyProfile(HttpServletRequest request) {

        String email = extractEmailFromRequest(request);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return convertToResponse(profile);
    }

    public ProfileResponse updateMyProfile(ProfileUpdateRequest updateRequest, HttpServletRequest request) {

        String email = extractEmailFromRequest(request);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setDisplayName(updateRequest.getDisplayName());
        profile.setBio(updateRequest.getBio());
        profile.setSkills(updateRequest.getSkills());
        profile.setLocation(updateRequest.getLocation());
        profile.setAvatarUrl(updateRequest.getAvatarUrl());

        Profile saved = profileRepository.save(profile);

        return convertToResponse(saved);
    }

    public ProfileResponse getProfileById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return convertToResponse(profile);
    }

    private String extractEmailFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No token provided");
        }

        String token = authHeader.substring(7);
        return jwtService.extractUsername(token);
    }

    private ProfileResponse convertToResponse(Profile profile) {
        ProfileResponse dto = new ProfileResponse();
        dto.setId(profile.getId());
        dto.setDisplayName(profile.getDisplayName());
        dto.setBio(profile.getBio());
        dto.setSkills(profile.getSkills());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setLocation(profile.getLocation());
        return dto;
    }
}

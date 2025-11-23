package org.platformv1.platformccv1.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.ProfileResponse;
import org.platformv1.platformccv1.dto.ProfileUpdateRequest;
import org.platformv1.platformccv1.services.ProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ProfileResponse getMyProfile(HttpServletRequest request) {
        return profileService.getMyProfile(request);
    }

    @PutMapping("/me")
    public ProfileResponse updateMyProfile(@RequestBody ProfileUpdateRequest updateRequest,
                                           HttpServletRequest request) {
        return profileService.updateMyProfile(updateRequest, request);
    }

    @GetMapping("/{id}")
    public ProfileResponse getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }
}

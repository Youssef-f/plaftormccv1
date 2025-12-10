package org.platformv1.platformccv1.services;

import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.AdminUserResponse;
import org.platformv1.platformccv1.dto.ChangeUserRoleRequest;
import org.platformv1.platformccv1.entity.Profile;
import org.platformv1.platformccv1.entity.User;
import org.platformv1.platformccv1.repository.ProfileRepository;
import org.platformv1.platformccv1.repository.ServiceListingRepository;
import org.platformv1.platformccv1.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ServiceListingRepository serviceListingRepository;

    /**
     * List all users with basic info + count of their services.
     */
    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toAdminUserResponse)
                .toList();
    }

    /**
     * Get one user by id.
     */
    public AdminUserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return toAdminUserResponse(user);
    }

    /**
     * Change the role of a user (ROLE_USER, ROLE_CREATOR, ROLE_ADMIN).
     */
    public AdminUserResponse changeUserRole(Long id, ChangeUserRoleRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newRole = req.getRole();

        if (!"ROLE_USER".equals(newRole) &&
                !"ROLE_ADMIN".equals(newRole) &&
                !"ROLE_CREATOR".equals(newRole)) {
            throw new IllegalArgumentException("Invalid role: " + newRole);
        }

        user.setRole(newRole);
        User saved = userRepository.save(user);

        return toAdminUserResponse(saved);
    }

    /**
     * Delete a user and all related data (profile + services).
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find profile
        Profile profile = profileRepository.findByUserId(user.getId()).orElse(null);

        if (profile != null) {
            // Delete all services owned by this profile
            serviceListingRepository.deleteAllByOwnerId(profile.getId());

            // Delete the profile itself
            profileRepository.delete(profile);
        }

        // Finally delete user
        userRepository.delete(user);
    }

    /**
     * Helper: convert User to DTO including profile + service count.
     */
    private AdminUserResponse toAdminUserResponse(User user) {
        AdminUserResponse dto = new AdminUserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());

        Profile profile = profileRepository.findByUserId(user.getId()).orElse(null);
        if (profile != null) {
            dto.setDisplayName(profile.getDisplayName());
            long serviceCount = serviceListingRepository.countByOwnerId(profile.getId());
            dto.setServiceCount((int) serviceCount);
        } else {
            dto.setDisplayName(null);
            dto.setServiceCount(0);
        }

        return dto;
    }
}
package org.platformv1.platformccv1.services;

import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.AdminStatsResponse;
import org.platformv1.platformccv1.repository.ProfileRepository;
import org.platformv1.platformccv1.repository.UserRepository;
import org.platformv1.platformccv1.repository.ServiceListingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ServiceListingRepository serviceRepository;

    public AdminStatsResponse getOverview() {

        AdminStatsResponse stats = new AdminStatsResponse();

        stats.setTotalUsers(userRepository.count());

        stats.setTotalCreators(profileRepository.count()); // later: filter by role = CREATOR

        stats.setTotalServices(serviceRepository.count());

        stats.setNewUsersThisMonth(
                userRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(30))
        );

        stats.setNewServicesThisMonth(
                serviceRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(30))
        );

        return stats;
    }
}
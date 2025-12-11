package org.platformv1.platformccv1.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.CreatorStatsResponse;
import org.platformv1.platformccv1.entity.Profile;
import org.platformv1.platformccv1.entity.ServiceListing;
import org.platformv1.platformccv1.entity.ServiceStatus;
import org.platformv1.platformccv1.entity.User;
import org.platformv1.platformccv1.repository.ProfileRepository;
import org.platformv1.platformccv1.repository.ServiceListingRepository;
import org.platformv1.platformccv1.repository.UserRepository;
import org.platformv1.platformccv1.security.JwtService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatorStatsService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ServiceListingRepository serviceRepo;

    public CreatorStatsResponse getMyStats(HttpServletRequest request) {
        // 1) Extract email
        String token = jwtService.extractToken(request);
        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        List<ServiceListing> services = serviceRepo.findByOwnerId(profile.getId());

        CreatorStatsResponse res = new CreatorStatsResponse();

        res.setTotalServices(services.size());
        res.setActiveServices(services.stream().filter(s -> s.getStatus() == ServiceStatus.ACTIVE).count());
        res.setDisabledServices(services.stream().filter(s -> s.getStatus() == ServiceStatus.DISABLED).count());
        res.setPendingServices(services.stream().filter(s -> s.getStatus() == ServiceStatus.PENDING_REVIEW).count());
        res.setTotalViews(services.stream().mapToLong(ServiceListing::getViewsCount).sum());

        return res;
    }
}
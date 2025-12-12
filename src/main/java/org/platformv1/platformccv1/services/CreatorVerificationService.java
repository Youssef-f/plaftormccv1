package org.platformv1.platformccv1.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.entity.CreatorVerificationRequest;
import org.platformv1.platformccv1.entity.Profile;
import org.platformv1.platformccv1.repository.CreatorVerificationRepository;
import org.platformv1.platformccv1.repository.ProfileRepository;
import org.platformv1.platformccv1.security.JwtService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorVerificationService {

    private final JwtService jwtService;
    private final ProfileRepository profileRepo;
    private final CreatorVerificationRepository repo;

    private Profile getLoggedProfile(HttpServletRequest request) {
        String email = jwtService.extractUsernameFromRequest(request);
        return profileRepo.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public void submit(HttpServletRequest request) {
        Profile profile = getLoggedProfile(request);

        if (repo.existsByCreatorId(profile.getId())) {
            throw new RuntimeException("Verification already submitted");
        }

        CreatorVerificationRequest req = new CreatorVerificationRequest();
        req.setCreatorId(profile.getId());
        req.setStatus("PENDING");

        repo.save(req);
    }

    public CreatorVerificationRequest getMyRequest(HttpServletRequest request) {
        Profile profile = getLoggedProfile(request);
        return repo.findByCreatorId(profile.getId())
                .orElseThrow(() -> new RuntimeException("No verification request"));
    }
}
package org.platformv1.platformccv1.services;

import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.entity.CreatorVerificationRequest;
import org.platformv1.platformccv1.repository.CreatorVerificationRepository;
import org.platformv1.platformccv1.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCreatorVerificationService {

    private final CreatorVerificationRepository repo;
    private final ProfileRepository profileRepository;

    public List<CreatorVerificationRequest> getAllPending() {
        return repo.findByStatus("PENDING");
    }

    @Transactional
    public void approve(Long id) {
        var req = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setStatus("APPROVED");
        repo.save(req);

        // Mark creator profile as verified
        var profile = profileRepository.findById(req.getCreatorId()).orElseThrow();
        profile.setVerified(true);
        profileRepository.save(profile);
    }

    @Transactional
    public void reject(Long id) {
        var req = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setStatus("REJECTED");
        repo.save(req);
    }
}


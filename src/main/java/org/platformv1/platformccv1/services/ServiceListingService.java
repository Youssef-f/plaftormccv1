package org.platformv1.platformccv1.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.ServiceListingRequest;
import org.platformv1.platformccv1.dto.ServiceListingResponse;
import org.platformv1.platformccv1.entity.Profile;
import org.platformv1.platformccv1.entity.ServiceListing;
import org.platformv1.platformccv1.entity.User;
import org.platformv1.platformccv1.repository.ProfileRepository;
import org.platformv1.platformccv1.repository.ServiceListingRepository;
import org.platformv1.platformccv1.repository.UserRepository;
import org.platformv1.platformccv1.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ServiceListingService {

    private final ServiceListingRepository serviceRepo;
    private final ProfileRepository profileRepo;
    private final JwtService jwtService;
    private final UserRepository userRepo;

    private String extractEmail(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = auth.substring(7);
        return jwtService.extractUsername(token);
    }

    private Profile getLoggedUserProfile(HttpServletRequest request) {
        String email = extractEmail(request);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return profileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public ServiceListingResponse create(ServiceListingRequest req, HttpServletRequest request) {

        Profile owner = getLoggedUserProfile(request);

        ServiceListing s = new ServiceListing();
        s.setOwner(owner);
        s.setTitle(req.getTitle());
        s.setDescription(req.getDescription());
        s.setPrice(req.getPrice());
        s.setTags(req.getTags());
        s.setDeliveryTime(req.getDeliveryTime());
        s.setCreatedAt(LocalDateTime.now());

        ServiceListing saved = serviceRepo.save(s);
        return toResponse(saved);
    }

    public List<ServiceListingResponse> getAll() {
        return serviceRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ServiceListingResponse getById(Long id) {
        ServiceListing s = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return toResponse(s);
    }

    public ServiceListingResponse update(Long id, ServiceListingRequest req, HttpServletRequest http) {
        String email = jwtService.extractUsername(jwtService.extractToken(http));
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceListing listing = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!listing.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        listing.setTitle(req.getTitle());
        listing.setDescription(req.getDescription());
        listing.setTags(req.getTags());
        listing.setPrice(req.getPrice());

        return toResponse(serviceRepo.save(listing));

    }

    public void delete(Long id, HttpServletRequest request) {
        Profile owner = getLoggedUserProfile(request);

        ServiceListing s = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!s.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Not authorized to delete this service");
        }

        serviceRepo.delete(s);
    }

    private ServiceListingResponse toResponse(ServiceListing s) {
        ServiceListingResponse dto = new ServiceListingResponse();
        dto.setId(s.getId());
        dto.setOwnerId(s.getOwner().getId());
        dto.setOwnerName(s.getOwner().getDisplayName());
        dto.setTitle(s.getTitle());
        dto.setDescription(s.getDescription());
        dto.setPrice(s.getPrice());
        dto.setTags(s.getTags());
        dto.setDeliveryTime(s.getDeliveryTime());
        return dto;
    }
}


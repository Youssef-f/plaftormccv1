package org.platformv1.platformccv1.services;

import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.ChangeServiceStatusRequest;
import org.platformv1.platformccv1.dto.ServiceListingResponse;
import org.platformv1.platformccv1.entity.ServiceListing;
import org.platformv1.platformccv1.entity.ServiceStatus;
import org.platformv1.platformccv1.repository.ServiceListingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceService {

    private final ServiceListingRepository serviceRepo;
    private final ServiceListingService serviceListingService;

    public List<ServiceListingResponse> getAllServices() {
        return serviceRepo.findAll()
                .stream()
                .map(serviceListingService::toResponsePublic) // we’ll expose a helper
                .toList();
    }

    public ServiceListingResponse changeStatus(Long id, ChangeServiceStatusRequest req) {
        ServiceListing s = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        ServiceStatus newStatus;
        try {
            newStatus = ServiceStatus.valueOf(req.getStatus());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid status: " + req.getStatus());
        }

        s.setStatus(newStatus);
        ServiceListing saved = serviceRepo.save(s);

        return serviceListingService.toResponsePublic(saved);
    }
    public List<ServiceListingResponse> getByStatus(String status) {
        return serviceRepo.findByStatus(ServiceStatus.valueOf(status))
                .stream()
                .map(serviceListingService::toResponsePublic)
                .toList();
    }
    public void deleteService(Long id) {
        if (!serviceRepo.existsById(id)) {
            throw new RuntimeException("Service not found");
        }
        serviceRepo.deleteById(id);
    }
    @Transactional
    public void updateStatus(Long id, String status) {
        ServiceListing s = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        s.setStatus(ServiceStatus.valueOf(status));
        serviceRepo.save(s);
    }

}
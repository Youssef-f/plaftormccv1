package org.platformv1.platformccv1.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.ServiceListingRequest;
import org.platformv1.platformccv1.dto.ServiceListingResponse;
import org.platformv1.platformccv1.services.ServiceListingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceListingController {

    private final ServiceListingService serviceService;

    @PostMapping
    public ServiceListingResponse create(@RequestBody ServiceListingRequest req,
                                         HttpServletRequest request) {
        return serviceService.create(req, request);
    }

    @GetMapping
    public List<ServiceListingResponse> getAll() {
        return serviceService.getAll();
    }

    @GetMapping("/{id}")
    public ServiceListingResponse getById(@PathVariable Long id) {
        return serviceService.getById(id);
    }

    @PutMapping("/{id}")
    public ServiceListingResponse update(@PathVariable Long id,
                                         @RequestBody ServiceListingRequest req,
                                         HttpServletRequest request) {
        return serviceService.update(id, req, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpServletRequest request) {
        serviceService.delete(id, request);
    }
}


package org.platformv1.platformccv1.controller;

import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.entity.CreatorVerificationRequest;
import org.platformv1.platformccv1.services.AdminCreatorVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/creators")
@RequiredArgsConstructor
public class AdminCreatorVerificationController {

    private final AdminCreatorVerificationService service;

    @GetMapping("/verifications")
    public List<CreatorVerificationRequest> getPending() {
        return service.getAllPending();
    }

    @PutMapping("/verifications/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        service.approve(id);
        return ResponseEntity.ok("Creator verified");
    }

    @PutMapping("/verifications/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        service.reject(id);
        return ResponseEntity.ok("Creator rejected");
    }
}


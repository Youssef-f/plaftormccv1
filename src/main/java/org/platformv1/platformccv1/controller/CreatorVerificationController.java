package org.platformv1.platformccv1.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.entity.CreatorVerificationRequest;
import org.platformv1.platformccv1.services.CreatorVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/creator-verification")
@RequiredArgsConstructor
public class CreatorVerificationController {

    private final CreatorVerificationService service;

    @PostMapping
    public ResponseEntity<?> submitVerification(HttpServletRequest request) {
        service.submit(request);
        return ResponseEntity.ok("Verification request submitted");
    }

    @GetMapping("/me")
    public CreatorVerificationRequest getMyRequest(HttpServletRequest request) {
        return service.getMyRequest(request);
    }
}

package org.platformv1.platformccv1.controller;

import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.ChangeServiceStatusRequest;
import org.platformv1.platformccv1.dto.ServiceListingResponse;
import org.platformv1.platformccv1.entity.ServiceStatus;
import org.platformv1.platformccv1.services.AdminServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
public class AdminServiceController {

    private final AdminServiceService adminServiceService;

    @GetMapping
    public ResponseEntity<?> getByStatus(@RequestParam(defaultValue = "PENDING_REVIEW") String status) {
        try {
            ServiceStatus enumStatus = ServiceStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(adminServiceService.getByStatus(enumStatus.name()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        }
    }

    // PUT /api/admin/services/{id}/approve
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        adminServiceService.updateStatus(id, ServiceStatus.ACTIVE.name());
        return ResponseEntity.ok("Service approved");
    }

    // PUT /api/admin/services/{id}/reject
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        adminServiceService.updateStatus(id, ServiceStatus.REJECTED.name());
        return ResponseEntity.ok("Service rejected");
    }

    // DELETE /api/admin/services/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        adminServiceService.deleteService(id);
        return ResponseEntity.ok("Service deleted");
    }

    // PATCH /api/admin/services/{id}/status
    @PatchMapping("/{id}/status")
    public ServiceListingResponse changeStatus(
            @PathVariable Long id,
            @RequestBody ChangeServiceStatusRequest request
    ) {
        return adminServiceService.changeStatus(id, request);
    }
}

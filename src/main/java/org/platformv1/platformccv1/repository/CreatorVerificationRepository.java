package org.platformv1.platformccv1.repository;

import org.platformv1.platformccv1.entity.CreatorVerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreatorVerificationRepository
        extends JpaRepository<CreatorVerificationRequest, Long> {
    List<CreatorVerificationRequest> findByStatus(String status);
}


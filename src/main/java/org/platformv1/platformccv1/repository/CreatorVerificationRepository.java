package org.platformv1.platformccv1.repository;

import org.platformv1.platformccv1.entity.CreatorVerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreatorVerificationRepository extends JpaRepository<CreatorVerificationRequest, Long> {

    Optional<CreatorVerificationRequest> findByCreatorId(Long creatorId);

    boolean existsByCreatorId(Long creatorId);

    List<CreatorVerificationRequest> findByStatus(String status);
}


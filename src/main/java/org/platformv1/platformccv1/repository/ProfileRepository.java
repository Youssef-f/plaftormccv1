package org.platformv1.platformccv1.repository;

import org.platformv1.platformccv1.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(Long userId);
    Optional<Profile> findByUser_Email(String email);

}

package org.platformv1.platformccv1.repository;

import org.platformv1.platformccv1.entity.ServiceListing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceListingRepository extends JpaRepository<ServiceListing, Long> {

    List<ServiceListing> findByTitleContainingIgnoreCase(String title);

    List<ServiceListing> findByTagsContainingIgnoreCase(String tag);

    List<ServiceListing> findByOwnerId(Long ownerId);
}

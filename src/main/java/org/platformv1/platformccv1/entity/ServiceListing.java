package org.platformv1.platformccv1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ServiceListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile owner;

    private String title;
    private String description;
    private Double price;
    private String tags;
    private Integer deliveryTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status = ServiceStatus.PENDING_REVIEW;

    @Column(nullable = false)
    private long viewsCount = 0L;

    private LocalDateTime createdAt;
}

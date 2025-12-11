package org.platformv1.platformccv1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CreatorVerificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long creatorId;

    private String message;

    private String status;  // <-- REQUIRED for .setStatus() to work

    private LocalDateTime createdAt;
}

package org.platformv1.platformccv1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String displayName;
    private String bio;
    private String avatarUrl;
    private String skills;
    private String location;
    private boolean verified = false;

    private LocalDateTime createdAt;
}
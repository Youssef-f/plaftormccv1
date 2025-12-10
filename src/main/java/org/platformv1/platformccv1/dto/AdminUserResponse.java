package org.platformv1.platformccv1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String displayName;
    private int serviceCount;
    private LocalDateTime createdAt;
}
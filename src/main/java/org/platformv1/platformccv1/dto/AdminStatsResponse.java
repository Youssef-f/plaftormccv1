package org.platformv1.platformccv1.dto;

import lombok.Data;

@Data
public class AdminStatsResponse {
    private long totalUsers;
    private long totalCreators;
    private long totalServices;
    private long newUsersThisMonth;
    private long newServicesThisMonth;
}

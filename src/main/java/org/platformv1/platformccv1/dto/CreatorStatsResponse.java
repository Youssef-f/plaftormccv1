package org.platformv1.platformccv1.dto;

import lombok.Data;

@Data
public class CreatorStatsResponse {
    private long totalServices;
    private long activeServices;
    private long disabledServices;
    private long pendingServices;
    private long totalViews;
}
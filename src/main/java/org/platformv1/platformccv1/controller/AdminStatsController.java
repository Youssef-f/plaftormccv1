package org.platformv1.platformccv1.controller;

import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.AdminStatsResponse;
import org.platformv1.platformccv1.services.AdminStatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsService statsService;

    @GetMapping("/overview")
    public AdminStatsResponse getOverview() {
        return statsService.getOverview();
    }
}

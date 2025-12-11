
package org.platformv1.platformccv1.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.CreatorStatsResponse;
import org.platformv1.platformccv1.services.CreatorStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorStatsService creatorStatsService;

    @GetMapping("/stats")
    public CreatorStatsResponse getMyStats(HttpServletRequest request) {
        return creatorStatsService.getMyStats(request);
    }
}

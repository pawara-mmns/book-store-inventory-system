package com.pawara.bookstore.controller;

import com.pawara.bookstore.dto.dashboard.DashboardResponseDto;
import com.pawara.bookstore.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Dashboard", description = "Dashboard APIs (Admin only)")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Get dashboard data", description = "Retrieve dashboard statistics (Admin only)")
    public ResponseEntity<DashboardResponseDto> getDashboardData() {
        DashboardResponseDto response = dashboardService.getDashboardData();
        return ResponseEntity.ok(response);
    }
}

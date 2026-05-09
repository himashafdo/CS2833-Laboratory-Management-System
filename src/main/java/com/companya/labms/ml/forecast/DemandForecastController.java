package com.companya.labms.ml.forecast;

import com.companya.labms.auth.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/ml/forecast")
@CrossOrigin(origins = "*")
public class DemandForecastController {

    private final DemandForecastService forecastService;
    private final JwtUtil jwtUtil;

    public DemandForecastController(DemandForecastService forecastService,
                                     JwtUtil jwtUtil) {
        this.forecastService = forecastService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * GET /api/ml/forecast/demand
     * Admin + Lab Technician: full demand forecast with heatmap data.
     * Returns { enabled: false, message: "..." } if feature is toggled off.
     */
    @GetMapping("/demand")
    public ResponseEntity<?> getDemandForecast(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String role = jwtUtil.extractRole(authHeader.substring(7));
            if (!isPrivileged(role)) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }
            return ResponseEntity.ok(forecastService.getForecast());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/ml/forecast/demand/peek
     * Lightweight version — just top 5 demand items.
     * Used for dashboard widgets.
     * Admin + Lab Technician only.
     */
    @GetMapping("/demand/peek")
    public ResponseEntity<?> getDemandPeek(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String role = jwtUtil.extractRole(authHeader.substring(7));
            if (!isPrivileged(role)) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }
            Map<String, Object> full = forecastService.getForecast();
            // Return only enabled status + top 5 + equipment ranking
            Map<String, Object> peek = new java.util.LinkedHashMap<>();
            peek.put("enabled", full.get("enabled"));
            peek.put("generatedAt", full.get("generatedAt"));
            if (Boolean.TRUE.equals(full.get("enabled"))) {
                java.util.List<?> top = (java.util.List<?>) full.get("topDemand");
                peek.put("topDemand", top != null ? top.stream().limit(5).toList() : java.util.List.of());
                peek.put("equipmentRanking", full.get("equipmentRanking"));
            } else {
                peek.put("message", full.get("message"));
            }
            return ResponseEntity.ok(peek);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private boolean isPrivileged(String role) {
        return "ADMIN".equals(role) || "LAB_TECHNICIAN".equals(role);
    }
}

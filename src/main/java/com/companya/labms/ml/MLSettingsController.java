package com.companya.labms.ml;

import com.companya.labms.auth.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ml/settings")
@CrossOrigin(origins = "*")
public class MLSettingsController {

    private final MLSettingsService service;
    private final JwtUtil jwtUtil;

    public MLSettingsController(MLSettingsService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    /**
     * GET /api/ml/settings
     * Admin + Lab Technician: view all ML feature toggles.
     */
    @GetMapping
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authHeader) {
        try {
            String role = extractRole(authHeader);
            if (!isPrivileged(role)) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }
            return ResponseEntity.ok(service.getAllSettings());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/ml/settings/status
     * Public-ish: returns just enabled/disabled map (safe for frontend feature-flagging).
     * Students can call this to know if a feature is available — no sensitive data.
     */
    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        try {
            return ResponseEntity.ok(service.getAllEnabledMap());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PATCH /api/ml/settings/{featureName}/toggle
     * Admin + Lab Technician only: flip enabled/disabled.
     */
    @PatchMapping("/{featureName}/toggle")
    public ResponseEntity<?> toggle(
            @PathVariable String featureName,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String role = extractRole(authHeader);
            if (!isPrivileged(role)) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }
            String username = extractUsername(authHeader);
            MLSettings updated = service.toggle(featureName, username);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PATCH /api/ml/settings/{featureName}/set
     * Admin + Lab Technician only: explicitly set enabled = true/false.
     * Body: { "enabled": true }
     */
    @PatchMapping("/{featureName}/set")
    public ResponseEntity<?> set(
            @PathVariable String featureName,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Boolean> body) {
        try {
            String role = extractRole(authHeader);
            if (!isPrivileged(role)) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }
            String username = extractUsername(authHeader);
            boolean enabled = body.getOrDefault("enabled", false);
            MLSettings updated = service.setEnabled(featureName, enabled, username);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private boolean isPrivileged(String role) {
        return "ADMIN".equals(role) || "LAB_TECHNICIAN".equals(role);
    }

    private String extractUsername(String authHeader) {
        return jwtUtil.extractUsername(authHeader.substring(7));
    }

    private String extractRole(String authHeader) {
        return jwtUtil.extractRole(authHeader.substring(7));
    }
}

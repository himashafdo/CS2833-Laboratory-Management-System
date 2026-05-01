package com.companya.labms.request;

import com.companya.labms.auth.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class EquipmentRequestController {

    private final EquipmentRequestService service;
    private final JwtUtil jwtUtil;

    public EquipmentRequestController(EquipmentRequestService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    /** POST /api/requests — Student submits a request */
    @PostMapping
    public ResponseEntity<?> submit(@RequestHeader("Authorization") String authHeader,
                                    @RequestBody EquipmentRequestDTO dto) {
        try {
            String username = extractUsername(authHeader);
            return ResponseEntity.ok(service.submitRequest(username, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** GET /api/requests/my — Student views own requests */
    @GetMapping("/my")
    public ResponseEntity<?> myRequests(@RequestHeader("Authorization") String authHeader) {
        try {
            String username = extractUsername(authHeader);
            return ResponseEntity.ok(service.getMyRequests(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** GET /api/requests — Admin/Technician views all requests */
    @GetMapping
    public ResponseEntity<?> all(@RequestHeader("Authorization") String authHeader) {
        try {
            String role = extractRole(authHeader);
            if (!role.equals("LAB_TECHNICIAN") && !role.equals("ADMIN")) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }
            return ResponseEntity.ok(service.getAllRequests());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** PUT /api/requests/{id}/approve — Admin approves */
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@RequestHeader("Authorization") String authHeader,
                                     @PathVariable Long id,
                                     @RequestBody(required = false) Map<String, String> body) {
        try {
            String role = extractRole(authHeader);
            if (!role.equals("LAB_TECHNICIAN") && !role.equals("ADMIN")) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }
            String note = body != null ? body.get("adminNote") : null;
            return ResponseEntity.ok(service.approveRequest(id, note));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** PUT /api/requests/{id}/reject — Admin rejects */
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@RequestHeader("Authorization") String authHeader,
                                    @PathVariable Long id,
                                    @RequestBody(required = false) Map<String, String> body) {
        try {
            String role = extractRole(authHeader);
            if (!role.equals("LAB_TECHNICIAN") && !role.equals("ADMIN")) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }
            String note = body != null ? body.get("adminNote") : null;
            return ResponseEntity.ok(service.rejectRequest(id, note));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** GET /api/requests/stats — Chart data */
    @GetMapping("/stats")
    public ResponseEntity<?> stats(@RequestHeader("Authorization") String authHeader) {
        try {
            return ResponseEntity.ok(service.getStats());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private String extractUsername(String authHeader) {
        return jwtUtil.extractUsername(authHeader.substring(7));
    }

    private String extractRole(String authHeader) {
        return jwtUtil.extractRole(authHeader.substring(7));
    }
}

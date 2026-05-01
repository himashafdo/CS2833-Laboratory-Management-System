package com.companya.labms.reservation;

import com.companya.labms.auth.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;
    private final JwtUtil jwtUtil;

    public ReservationController(ReservationService reservationService,
                                  JwtUtil jwtUtil) {
        this.reservationService = reservationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyReservations(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String username = jwtUtil.extractUsername(authHeader.substring(7));
            return ResponseEntity.ok(reservationService.getUserReservations(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<Reservation>> getEquipmentReservations(
            @PathVariable Long equipmentId) {
        return ResponseEntity.ok(reservationService.getEquipmentReservations(equipmentId));
    }

    @GetMapping("/lab/{labId}")
    public ResponseEntity<List<Reservation>> getLabReservations(
            @PathVariable Long labId) {
        return ResponseEntity.ok(reservationService.getLabReservations(labId));
    }

    @PostMapping
    public ResponseEntity<?> createReservation(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> body) {
        try {
            String username = jwtUtil.extractUsername(authHeader.substring(7));

            Long equipmentId = body.get("equipmentId") != null ?
                    Long.valueOf(body.get("equipmentId").toString()) : null;
            Long labId = body.get("labId") != null ?
                    Long.valueOf(body.get("labId").toString()) : null;
            LocalDateTime start = LocalDateTime.parse(body.get("startTime").toString());
            LocalDateTime end = LocalDateTime.parse(body.get("endTime").toString());
            int workstations = body.get("quantity") != null ?
                    Integer.parseInt(body.get("quantity").toString()) : 1;

            Reservation reservation = reservationService.createReservation(
                    username, equipmentId, labId, start, end, workstations);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String username = jwtUtil.extractUsername(authHeader.substring(7));
            return ResponseEntity.ok(reservationService.cancelReservation(id, username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            Reservation.ReservationStatus status =
                    Reservation.ReservationStatus.valueOf(body.get("status").toUpperCase());
            return ResponseEntity.ok(reservationService.updateStatus(id, status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
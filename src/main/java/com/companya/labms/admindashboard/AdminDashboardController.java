package com.companya.labms.admindashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.companya.labms.catalog.Equipment;
import com.companya.labms.reservation.Reservation;
import com.companya.labms.ticketing.Issue;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notices")
public class AdminDashboardController {

    private final AdminDashboardService service;

    public AdminDashboardController(AdminDashboardService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AdminDashboard>> getAllNotices() {
        return ResponseEntity.ok(service.getAllNotices());
    }

    @PostMapping
    public ResponseEntity<AdminDashboard> createNotice(@RequestBody AdminDashboard notice) {
        return ResponseEntity.ok(service.createNotice(notice));
    }

    @GetMapping("/stats")
    public ResponseEntity<java.util.Map<String, Long>> getStats() {
        return ResponseEntity.ok(service.getDashboardStats());
    }

    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> getEquipment() {
        return ResponseEntity.ok(service.getAllEquipment());
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok(service.getAllReservations());
    }

    @GetMapping("/issues")
    public ResponseEntity<List<Issue>> getIssues() {
        return ResponseEntity.ok(service.getAllIssues());
    }
}
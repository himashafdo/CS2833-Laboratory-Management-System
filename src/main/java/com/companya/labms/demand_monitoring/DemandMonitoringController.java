package com.companya.labms.demand_monitoring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.companya.labms.demand_monitoring.DemandMonitoringDTO.*;
import com.companya.labms.demand_monitoring.DemandMonitoringService;

/**
 * DemandMonitoringController
 *
 * Base URL: /api/demand
 * All endpoints return JSON. CORS is open for local dev —
 * tighten in production or via your global CORS config.
 *
 * Replace "com.labms" with your actual base package.
 */
@RestController
@RequestMapping("/api/demand")
@CrossOrigin(origins = "*")   // remove / restrict in production
public class DemandMonitoringController {

    private final DemandMonitoringService service;

    public DemandMonitoringController(DemandMonitoringService service) {
        this.service = service;
    }

    /**
     * GET /api/demand/dashboard
     * Returns the full dashboard payload in one call.
     * The frontend uses this single endpoint to populate all charts.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> getDashboard() {
        return ResponseEntity.ok(service.getDashboard());
    }

    /**
     * GET /api/demand/summary
     * Summary card numbers only (fast, lightweight).
     */
    @GetMapping("/summary")
    public ResponseEntity<SummaryDTO> getSummary() {
        return ResponseEntity.ok(service.getSummary());
    }

    /**
     * GET /api/demand/equipment-usage
     * Most-used equipment list (bar chart data).
     */
    @GetMapping("/equipment-usage")
    public ResponseEntity<List<EquipmentUsageDTO>> getEquipmentUsage() {
        return ResponseEntity.ok(service.getEquipmentUsage());
    }

    /**
     * GET /api/demand/monthly-trend
     * Reservation counts grouped by month (line chart data).
     */
    @GetMapping("/monthly-trend")
    public ResponseEntity<List<UsageTrendDTO>> getMonthlyTrend() {
        return ResponseEntity.ok(service.getMonthlyTrend());
    }

    /**
     * GET /api/demand/peak-hours
     * Reservation density by day-of-week × hour (heatmap data).
     */
    @GetMapping("/peak-hours")
    public ResponseEntity<List<PeakHourDTO>> getPeakHours() {
        return ResponseEntity.ok(service.getPeakHours());
    }

    /**
     * GET /api/demand/lab-usage
     * Reservation count per lab (for lab utilisation panel).
     */
    @GetMapping("/lab-usage")
    public ResponseEntity<List<LabUsageDTO>> getLabUsage() {
        return ResponseEntity.ok(service.getLabUsage());
    }
}

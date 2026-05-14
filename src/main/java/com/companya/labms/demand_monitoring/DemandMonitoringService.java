package com.companya.labms.demand_monitoring;

import com.companya.labms.demand_monitoring.DemandMonitoringDTO.*;
import com.companya.labms.demand_monitoring.DemandMonitoringRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DemandMonitoringService
 *
 * Converts raw Object[] query results into typed DTOs.
 * Replace "com.labms" with your actual base package.
 */
@Service
public class DemandMonitoringService {

    private final DemandMonitoringRepository repo;

    public DemandMonitoringService(DemandMonitoringRepository repo) {
        this.repo = repo;
    }

    // ── Full dashboard (single endpoint call) ─────────────────────────────
    public DashboardDTO getDashboard() {
        return new DashboardDTO(
            getSummary(),
            getEquipmentUsage(),
            getMonthlyTrend(),
            getPeakHours(),
            getLabUsage()
        );
    }

    // ── Summary cards ─────────────────────────────────────────────────────
    public SummaryDTO getSummary() {
        long totalRes   = safeCount(repo.countTotalReservations());
        long activeRes  = safeCount(repo.countActiveReservations());
        long totalEq    = safeCount(repo.countTotalEquipment());
        long availEq    = safeCount(repo.countAvailableEquipment());
        long totalLabs  = safeCount(repo.countTotalLabs());
        long openTix    = safeCount(repo.countOpenTickets());
        return new SummaryDTO(totalRes, activeRes, totalEq, availEq, totalLabs, openTix);
    }

    // ── Most-used equipment ───────────────────────────────────────────────
    public List<EquipmentUsageDTO> getEquipmentUsage() {
        return repo.findEquipmentUsageRaw().stream()
            .map(row -> new EquipmentUsageDTO(
                str(row[0]),
                toLong(row[1]),
                str(row[2])
            ))
            .collect(Collectors.toList());
    }

    // ── Monthly trend ─────────────────────────────────────────────────────
    public List<UsageTrendDTO> getMonthlyTrend() {
        return repo.findMonthlyTrendRaw().stream()
            .map(row -> new UsageTrendDTO(str(row[0]), toLong(row[1])))
            .collect(Collectors.toList());
    }

    // ── Peak hours heatmap ────────────────────────────────────────────────
    public List<PeakHourDTO> getPeakHours() {
        return repo.findPeakHoursRaw().stream()
            .map(row -> new PeakHourDTO(
                toInt(row[0]),
                toInt(row[1]),
                toLong(row[2])
            ))
            .collect(Collectors.toList());
    }

    // ── Lab usage ─────────────────────────────────────────────────────────
    public List<LabUsageDTO> getLabUsage() {
        return repo.findLabUsageRaw().stream()
            .map(row -> new LabUsageDTO(str(row[0]), toLong(row[1])))
            .collect(Collectors.toList());
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private long safeCount(Long value) {
        return value != null ? value : 0L;
    }

    private String str(Object o) {
        return o != null ? o.toString() : "";
    }

    private long toLong(Object o) {
        if (o == null) return 0L;
        if (o instanceof Number) return ((Number) o).longValue();
        try { return Long.parseLong(o.toString()); } catch (Exception e) { return 0L; }
    }

    private int toInt(Object o) {
        if (o == null) return 0;
        if (o instanceof Number) return ((Number) o).intValue();
        try { return Integer.parseInt(o.toString()); } catch (Exception e) { return 0; }
    }
}

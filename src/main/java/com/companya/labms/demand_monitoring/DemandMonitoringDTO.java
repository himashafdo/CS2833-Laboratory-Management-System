package com.companya.labms.demand_monitoring;

import java.util.List;

/**
 * DTOs for Demand Monitoring API responses.
 * Replace "com.labms" with your actual base package name.
 */
public class DemandMonitoringDTO {

    // ── Equipment usage frequency (Bar Chart) ──────────────────────────────
    public static class EquipmentUsageDTO {
        private String equipmentName;
        private Long reservationCount;
        private String category;

        public EquipmentUsageDTO(String equipmentName, Long reservationCount, String category) {
            this.equipmentName = equipmentName;
            this.reservationCount = reservationCount;
            this.category = category;
        }

        public String getEquipmentName() { return equipmentName; }
        public Long getReservationCount() { return reservationCount; }
        public String getCategory() { return category; }
    }

    // ── Usage trend over time (Line Chart) ─────────────────────────────────
    public static class UsageTrendDTO {
        private String period;        // e.g. "2026-01", "2026-04-Week1"
        private Long reservationCount;

        public UsageTrendDTO(String period, Long reservationCount) {
            this.period = period;
            this.reservationCount = reservationCount;
        }

        public String getPeriod() { return period; }
        public Long getReservationCount() { return reservationCount; }
    }

    // ── Peak hours heatmap (hour 0-23, day 0-6) ────────────────────────────
    public static class PeakHourDTO {
        private int dayOfWeek;        // 1=Sunday … 7=Saturday (MySQL DAYOFWEEK)
        private int hour;             // 0 – 23
        private Long count;

        public PeakHourDTO(int dayOfWeek, int hour, Long count) {
            this.dayOfWeek = dayOfWeek;
            this.hour = hour;
            this.count = count;
        }

        public int getDayOfWeek() { return dayOfWeek; }
        public int getHour() { return hour; }
        public Long getCount() { return count; }
    }

    // ── Lab usage stats (for lab utilisation panel) ────────────────────────
    public static class LabUsageDTO {
        private String labName;
        private Long reservationCount;

        public LabUsageDTO(String labName, Long reservationCount) {
            this.labName = labName;
            this.reservationCount = reservationCount;
        }

        public String getLabName() { return labName; }
        public Long getReservationCount() { return reservationCount; }
    }

    // ── Top-level summary card numbers ─────────────────────────────────────
    public static class SummaryDTO {
        private long totalReservations;
        private long activeReservations;
        private long totalEquipment;
        private long availableEquipment;
        private long totalLabs;
        private long openTickets;

        public SummaryDTO(long totalReservations, long activeReservations,
                          long totalEquipment, long availableEquipment,
                          long totalLabs, long openTickets) {
            this.totalReservations   = totalReservations;
            this.activeReservations  = activeReservations;
            this.totalEquipment      = totalEquipment;
            this.availableEquipment  = availableEquipment;
            this.totalLabs           = totalLabs;
            this.openTickets         = openTickets;
        }

        public long getTotalReservations()  { return totalReservations; }
        public long getActiveReservations() { return activeReservations; }
        public long getTotalEquipment()     { return totalEquipment; }
        public long getAvailableEquipment() { return availableEquipment; }
        public long getTotalLabs()          { return totalLabs; }
        public long getOpenTickets()        { return openTickets; }
    }

    // ── Full dashboard response (one call gets everything) ─────────────────
    public static class DashboardDTO {
        private SummaryDTO summary;
        private List<EquipmentUsageDTO> equipmentUsage;
        private List<UsageTrendDTO> usageTrend;
        private List<PeakHourDTO> peakHours;
        private List<LabUsageDTO> labUsage;

        public DashboardDTO(SummaryDTO summary,
                            List<EquipmentUsageDTO> equipmentUsage,
                            List<UsageTrendDTO> usageTrend,
                            List<PeakHourDTO> peakHours,
                            List<LabUsageDTO> labUsage) {
            this.summary        = summary;
            this.equipmentUsage = equipmentUsage;
            this.usageTrend     = usageTrend;
            this.peakHours      = peakHours;
            this.labUsage       = labUsage;
        }

        public SummaryDTO getSummary()                    { return summary; }
        public List<EquipmentUsageDTO> getEquipmentUsage(){ return equipmentUsage; }
        public List<UsageTrendDTO> getUsageTrend()        { return usageTrend; }
        public List<PeakHourDTO> getPeakHours()           { return peakHours; }
        public List<LabUsageDTO> getLabUsage()            { return labUsage; }
    }
}

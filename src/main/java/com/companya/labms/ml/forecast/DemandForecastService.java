package com.companya.labms.ml.forecast;

import com.companya.labms.ml.MLSettingsService;
import com.companya.labms.reservation.Reservation;
import com.companya.labms.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DemandForecastService {

    private final ReservationRepository reservationRepository;
    private final MLSettingsService mlSettingsService;

    // How many weeks back to look for training data
    private static final int HISTORY_WEEKS = 8;

    // Decay factor — recent weeks count more than old ones
    private static final double DECAY = 0.85;

    public DemandForecastService(ReservationRepository reservationRepository,
                                  MLSettingsService mlSettingsService) {
        this.reservationRepository = reservationRepository;
        this.mlSettingsService = mlSettingsService;
    }

    /**
     * Main forecast method.
     * Returns demand scores per equipment type per time slot per day of week.
     * Score = weighted count of past reservations (recent weeks weighted higher).
     *
     * Result shape:
     * {
     *   "enabled": true,
     *   "generatedAt": "...",
     *   "topDemand": [ { equipmentName, dayOfWeek, hourSlot, score, level } ],
     *   "byEquipment": { "Oscilloscope DSO-X 1204G": { "MONDAY_09": 4.2, ... } },
     *   "peakHours": { "MONDAY": 9, "TUESDAY": 14, ... },
     *   "summary": { totalReservationsAnalysed: 120, weeksOfHistory: 8 }
     * }
     */
    public Map<String, Object> getForecast() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Check if feature is enabled
        boolean enabled = mlSettingsService.isEnabled("demand_forecast");
        result.put("enabled", enabled);
        result.put("generatedAt", LocalDateTime.now().toString());

        if (!enabled) {
            result.put("message", "Demand forecasting is currently disabled. Enable it in ML Settings.");
            return result;
        }

        // Pull reservations from last N weeks
        LocalDateTime since = LocalDateTime.now().minusWeeks(HISTORY_WEEKS);
        List<Reservation> history = reservationRepository.findAll().stream()
            .filter(r -> r.getStartTime() != null && r.getStartTime().isAfter(since))
            .filter(r -> r.getStatus() == Reservation.ReservationStatus.APPROVED
                      || r.getStatus() == Reservation.ReservationStatus.COMPLETED
                      || r.getStatus() == Reservation.ReservationStatus.PENDING)
            .collect(Collectors.toList());

        if (history.isEmpty()) {
            result.put("message", "Not enough reservation data to generate forecast. Make some bookings first.");
            result.put("topDemand", Collections.emptyList());
            return result;
        }

        // ── Build weighted demand map ──────────────────────────────────────────
        // Key: "EquipmentName|DAY_OF_WEEK|HOUR_SLOT"
        // Value: weighted score
        Map<String, Double> demandMap = new LinkedHashMap<>();

        LocalDateTime now = LocalDateTime.now();

        for (Reservation r : history) {
            if (r.getStartTime() == null) continue;

            // Equipment name — use "Lab Workstation" for lab-only bookings
            String equipName = (r.getEquipment() != null)
                ? r.getEquipment().getName()
                : "Lab Workstation";

            DayOfWeek day = r.getStartTime().getDayOfWeek();
            int hour = r.getStartTime().getHour();
            // Round to 2-hour slots: 8→8, 9→8, 10→10, 11→10 etc.
            int slot = (hour / 2) * 2;

            String key = equipName + "|" + day.name() + "|" + slot;

            // Decay weight — how many weeks ago was this?
            long weeksAgo = java.time.temporal.ChronoUnit.WEEKS.between(
                r.getStartTime(), now);
            weeksAgo = Math.min(weeksAgo, HISTORY_WEEKS);
            double weight = Math.pow(DECAY, weeksAgo);

            demandMap.merge(key, weight, Double::sum);
        }

        // ── Build byEquipment map ──────────────────────────────────────────────
        Map<String, Map<String, Double>> byEquipment = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : demandMap.entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            String name = parts[0];
            String slotKey = parts[1] + "_" + parts[2]; // e.g. MONDAY_9
            byEquipment.computeIfAbsent(name, k -> new LinkedHashMap<>())
                       .put(slotKey, Math.round(entry.getValue() * 100.0) / 100.0);
        }

        // ── Top demand list (sorted by score desc, top 20) ────────────────────
        List<Map<String, Object>> topDemand = demandMap.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(20)
            .map(e -> {
                String[] parts = e.getKey().split("\\|");
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("equipmentName", parts[0]);
                item.put("dayOfWeek", parts[1]);
                item.put("hourSlot", Integer.parseInt(parts[2]));
                item.put("hourLabel", formatHour(Integer.parseInt(parts[2])));
                item.put("score", Math.round(e.getValue() * 100.0) / 100.0);
                item.put("level", demandLevel(e.getValue()));
                return item;
            })
            .collect(Collectors.toList());

        // ── Peak hour per day ─────────────────────────────────────────────────
        Map<String, Integer> peakHours = new LinkedHashMap<>();
        Map<String, Double> daySlotTotals = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : demandMap.entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            String daySlot = parts[1] + "|" + parts[2];
            daySlotTotals.merge(daySlot, entry.getValue(), Double::sum);
        }
        // For each day, find the slot with highest total score
        Map<String, Map.Entry<String, Double>> bestPerDay = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : daySlotTotals.entrySet()) {
            String day = entry.getKey().split("\\|")[0];
            bestPerDay.merge(day, entry, (a, b) -> a.getValue() >= b.getValue() ? a : b);
        }
        for (Map.Entry<String, Map.Entry<String, Double>> e : bestPerDay.entrySet()) {
            int hour = Integer.parseInt(e.getValue().getKey().split("\\|")[1]);
            peakHours.put(e.getKey(), hour);
        }

        // ── Most demanded equipment overall ───────────────────────────────────
        Map<String, Double> equipTotals = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : demandMap.entrySet()) {
            String name = entry.getKey().split("\\|")[0];
            equipTotals.merge(name, entry.getValue(), Double::sum);
        }
        List<Map<String, Object>> equipRanking = equipTotals.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .map(e -> {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("equipmentName", e.getKey());
                item.put("totalScore", Math.round(e.getValue() * 100.0) / 100.0);
                item.put("level", demandLevel(e.getValue() / 7.0)); // normalise per day
                return item;
            })
            .collect(Collectors.toList());

        // ── Heatmap data (for chart rendering) ────────────────────────────────
        // Format: list of { day, hour, score } for all combinations
        List<Map<String, Object>> heatmap = daySlotTotals.entrySet().stream()
            .map(e -> {
                String[] parts = e.getKey().split("\\|");
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("day", parts[0]);
                item.put("hour", Integer.parseInt(parts[1]));
                item.put("hourLabel", formatHour(Integer.parseInt(parts[1])));
                item.put("score", Math.round(e.getValue() * 100.0) / 100.0);
                item.put("level", demandLevel(e.getValue()));
                return item;
            })
            .collect(Collectors.toList());

        // ── Summary ───────────────────────────────────────────────────────────
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalReservationsAnalysed", history.size());
        summary.put("weeksOfHistory", HISTORY_WEEKS);
        summary.put("uniqueEquipmentTypes", byEquipment.size());
        summary.put("decayFactor", DECAY);
        summary.put("note", "Recent weeks are weighted higher using exponential decay (factor=" + DECAY + ").");

        result.put("topDemand", topDemand);
        result.put("byEquipment", byEquipment);
        result.put("equipmentRanking", equipRanking);
        result.put("peakHours", peakHours);
        result.put("heatmap", heatmap);
        result.put("summary", summary);

        return result;
    }

    /**
     * Lightweight check — just returns whether the feature is on/off.
     * Safe to call from any other service.
     */
    public boolean isActive() {
        return mlSettingsService.isEnabled("demand_forecast");
    }

    private String demandLevel(double score) {
        if (score >= 5.0) return "HIGH";
        if (score >= 2.0) return "MEDIUM";
        return "LOW";
    }

    private String formatHour(int hour) {
        String suffix = hour >= 12 ? "PM" : "AM";
        int h = hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour);
        return h + ":00 " + suffix + " – " + (h + 2 > 12 ? h + 2 - 12 : h + 2) + ":00 " + (hour + 2 >= 12 ? "PM" : "AM");
    }
}

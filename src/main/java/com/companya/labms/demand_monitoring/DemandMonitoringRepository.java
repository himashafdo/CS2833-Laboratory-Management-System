package com.companya.labms.demand_monitoring;

import com.companya.labms.demand_monitoring.DemandMonitoringDTO.*;
import com.companya.labms.catalog.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DemandMonitoringRepository
 *
 * All queries run against the "labms" schema.
 * Replace "com.labms" with your actual base package.
 *
 * NOTE: This repository does NOT extend a specific entity — it uses
 * @Query with nativeQuery=true mapped to constructor-expression DTOs.
 * Attach it to any lightweight entity (e.g. Equipment) or use a
 * plain @Repository bean with EntityManager if you prefer.
 *
 * Simplest approach: attach to Equipment entity since that table
 * always exists. All the interesting queries JOIN to reservations.
 */
@Repository
public interface DemandMonitoringRepository extends JpaRepository<Equipment, Long> {

    // ── Most-used equipment (bar chart) ──────────────────────────────────
    // Falls back gracefully if reservations table has no rows yet.
    @Query(value = """
        SELECT e.name            AS equipmentName,
               COUNT(r.id)       AS reservationCount,
               e.description     AS category
        FROM   equipment e
        LEFT JOIN reservations r ON r.equipment_id = e.id
        GROUP BY e.id, e.name, e.description
        ORDER BY reservationCount DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findEquipmentUsageRaw();

    // ── Monthly usage trend (line chart) ─────────────────────────────────
    @Query(value = """
        SELECT DATE_FORMAT(r.created_at, '%Y-%m') AS period,
               COUNT(r.id)                         AS reservationCount
        FROM   reservations r
        GROUP BY period
        ORDER BY period ASC
        LIMIT 12
        """, nativeQuery = true)
    List<Object[]> findMonthlyTrendRaw();

    // ── Peak-hour heatmap (day × hour grid) ──────────────────────────────
    @Query(value = """
        SELECT DAYOFWEEK(r.created_at)  AS dayOfWeek,
               HOUR(r.created_at)       AS hour,
               COUNT(r.id)              AS cnt
        FROM   reservations r
        GROUP BY dayOfWeek, hour
        ORDER BY dayOfWeek, hour
        """, nativeQuery = true)
    List<Object[]> findPeakHoursRaw();

    // ── Lab reservation counts ────────────────────────────────────────────
    @Query(value = """
        SELECT l.lab_name         AS labName,
               COUNT(r.id)        AS reservationCount
        FROM   labs l
        LEFT JOIN reservations r ON r.lab_id = l.id
        GROUP BY l.id, l.lab_name
        ORDER BY reservationCount DESC
        """, nativeQuery = true)
    List<Object[]> findLabUsageRaw();

    // ── Summary card counts ───────────────────────────────────────────────
    @Query(value = "SELECT COUNT(*) FROM reservations", nativeQuery = true)
    Long countTotalReservations();

    @Query(value = "SELECT COUNT(*) FROM reservations WHERE status = 'ACTIVE'", nativeQuery = true)
    Long countActiveReservations();

    @Query(value = "SELECT COUNT(*) FROM equipment", nativeQuery = true)
    Long countTotalEquipment();

    @Query(value = "SELECT COUNT(*) FROM equipment WHERE status = 'AVAILABLE'", nativeQuery = true)
    Long countAvailableEquipment();

    @Query(value = "SELECT COUNT(*) FROM labs", nativeQuery = true)
    Long countTotalLabs();

    // Open tickets — use 0 if issues/tickets table not yet created
    @Query(value = """
        SELECT CASE
            WHEN COUNT(*) > 0 THEN (SELECT COUNT(*) FROM issues WHERE status = 'OPEN')
            ELSE 0
        END
        FROM information_schema.tables
        WHERE table_schema = 'labms' AND table_name = 'issues'
        """, nativeQuery = true)
    Long countOpenTickets();
}

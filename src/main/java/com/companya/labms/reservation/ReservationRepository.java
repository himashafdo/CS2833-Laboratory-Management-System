package com.companya.labms.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByEquipmentId(Long equipmentId);
    List<Reservation> findByLabId(Long labId);

    @Query("SELECT r FROM Reservation r WHERE r.equipment.id = :equipmentId AND r.status != 'CANCELLED' AND (r.startTime < :end AND r.endTime > :start)")
    List<Reservation> findConflictingEquipmentReservations(
        @Param("equipmentId") Long equipmentId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);

    @Query("SELECT r FROM Reservation r WHERE r.lab.id = :labId AND r.status != 'CANCELLED' AND (r.startTime < :end AND r.endTime > :start)")
    List<Reservation> findConflictingLabReservations(
        @Param("labId") Long labId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(r.workstations), 0) FROM Reservation r WHERE r.user.id = :userId AND r.lab.id = :labId AND r.status != 'CANCELLED' AND r.endTime > CURRENT_TIMESTAMP")
    int countActiveWorkstationsByUser(
        @Param("userId") Long userId,
        @Param("labId") Long labId);
}
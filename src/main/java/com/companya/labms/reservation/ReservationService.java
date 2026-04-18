package com.companya.labms.reservation;

import com.companya.labms.auth.User;
import com.companya.labms.auth.UserRepository;
import com.companya.labms.catalog.Equipment;
import com.companya.labms.catalog.EquipmentRepository;
import com.companya.labms.catalog.Lab;
import com.companya.labms.catalog.LabRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final LabRepository labRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              UserRepository userRepository,
                              EquipmentRepository equipmentRepository,
                              LabRepository labRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
        this.labRepository = labRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getUserReservations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUserId(user.getId());
    }

    public List<Reservation> getEquipmentReservations(Long equipmentId) {
        return reservationRepository.findByEquipmentId(equipmentId);
    }

    public List<Reservation> getLabReservations(Long labId) {
        return reservationRepository.findByLabId(labId);
    }

    public Reservation createReservation(String username, Long equipmentId,
                                          Long labId, LocalDateTime start,
                                          LocalDateTime end, int workstations) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate times
        if (!start.isBefore(end))
            throw new RuntimeException("End time must be after start time");
        if (start.isBefore(LocalDateTime.now()))
            throw new RuntimeException("Cannot book in the past");

        // Check workstation limit (max 3 per user per lab)
        if (labId != null && equipmentId == null) {
            int current = reservationRepository.countActiveWorkstationsByUser(user.getId(), labId);
            if (current + workstations > 3)
                throw new RuntimeException("You can only reserve up to 3 workstations at a time. You currently have " + current + " reserved.");
        }

        // Check equipment conflict
        if (equipmentId != null) {
            List<Reservation> conflicts = reservationRepository
                    .findConflictingEquipmentReservations(equipmentId, start, end);
            if (!conflicts.isEmpty())
                throw new RuntimeException("This equipment is already booked for the selected time slot");
        }

        // Check lab conflict
        if (labId != null && equipmentId == null) {
            List<Reservation> conflicts = reservationRepository
                    .findConflictingLabReservations(labId, start, end);
            if (!conflicts.isEmpty())
                throw new RuntimeException("This lab is already booked for the selected time slot");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setWorkstations(workstations);
        reservation.setStatus(Reservation.ReservationStatus.PENDING);

        if (equipmentId != null) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("Equipment not found"));
            reservation.setEquipment(equipment);
        }

        if (labId != null) {
            Lab lab = labRepository.findById(labId)
                    .orElseThrow(() -> new RuntimeException("Lab not found"));
            reservation.setLab(lab);
        }

        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long id, String username) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        if (!reservation.getUser().getUsername().equals(username))
            throw new RuntimeException("You can only cancel your own reservations");
        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    public Reservation updateStatus(Long id, Reservation.ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }
}
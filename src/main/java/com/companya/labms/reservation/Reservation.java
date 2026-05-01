package com.companya.labms.reservation;

import com.companya.labms.auth.User;
import com.companya.labms.catalog.Equipment;
import com.companya.labms.catalog.Lab;
import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Lab lab;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    private int workstations = 1;

    public enum ReservationStatus { PENDING, APPROVED, CANCELLED, COMPLETED }


    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }
    public Lab getLab() { return lab; }
    public void setLab(Lab lab) { this.lab = lab; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    

    public int getWorkstations() { return workstations; }
    public void setWorkstations(int workstations) { this.workstations = workstations; }
}
package com.companya.labms.request;

import com.companya.labms.auth.User;
import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "requests")
public class EquipmentRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String equipmentDetails;
    private String justification;
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private UrgencyLevel urgencyLevel;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    public enum RequestStatus { PENDING, APPROVED, REJECTED }
    public enum UrgencyLevel { LOW, MEDIUM, HIGH, CRITICAL }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getEquipmentDetails() { return equipmentDetails; }
    public void setEquipmentDetails(String equipmentDetails) { this.equipmentDetails = equipmentDetails; }
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public UrgencyLevel getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
}
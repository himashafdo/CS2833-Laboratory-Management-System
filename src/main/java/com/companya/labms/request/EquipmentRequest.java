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

    private String itemName;
    private String description;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    public enum RequestStatus { PENDING, APPROVED, REJECTED }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
}
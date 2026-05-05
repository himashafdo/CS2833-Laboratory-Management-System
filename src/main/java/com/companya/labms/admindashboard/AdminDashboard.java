package com.companya.labms.admindashboard;

import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_notices")
public class AdminDashboard extends BaseEntity {

    private String title;
    private String message;
    private boolean isActive;

    public AdminDashboard() {}

    public AdminDashboard(String title, String message, boolean isActive) {
        this.title = title;
        this.message = message;
        this.isActive = isActive;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}

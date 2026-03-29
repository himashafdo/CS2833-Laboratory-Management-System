package com.companya.labms.catalog;

import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "labs")
public class Lab extends BaseEntity {

    private String labName;
    private String location;

    @Enumerated(EnumType.STRING)
    private LabStatus status = LabStatus.AVAILABLE;

    public enum LabStatus { AVAILABLE, IN_USE, MAINTENANCE }

    public String getLabName() { return labName; }
    public void setLabName(String labName) { this.labName = labName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LabStatus getStatus() { return status; }
    public void setStatus(LabStatus status) { this.status = status; }
}
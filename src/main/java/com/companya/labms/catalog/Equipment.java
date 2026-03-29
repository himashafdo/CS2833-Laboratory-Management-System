package com.companya.labms.catalog;

import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "equipment")
public class Equipment extends BaseEntity {

    private String name;
    private String description;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    public enum EquipmentStatus { AVAILABLE, RESERVED, MAINTENANCE, UNAVAILABLE }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public EquipmentStatus getStatus() { return status; }
    public void setStatus(EquipmentStatus status) { this.status = status; }
}
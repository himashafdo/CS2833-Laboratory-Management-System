package com.companya.labms.catalog;

import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "equipment")
public class Equipment extends BaseEntity {

    private String name;
    private String description;
    private String imageUrl;

    // From feature/admin-catalog branch
    private String serialNumber;

    // From feature/admin-catalog branch
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_type_id")
    private EquipmentType equipmentType;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    private int quantity = 1;

    // From main branch (individual unit tracking)
    private String equipmentCode;

    public enum EquipmentStatus { AVAILABLE, RESERVED, MAINTENANCE, UNAVAILABLE }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public EquipmentType getEquipmentType() { return equipmentType; }
    public void setEquipmentType(EquipmentType equipmentType) { this.equipmentType = equipmentType; }

    public EquipmentStatus getStatus() { return status; }
    public void setStatus(EquipmentStatus status) { this.status = status; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getEquipmentCode() { return equipmentCode; }
    public void setEquipmentCode(String equipmentCode) { this.equipmentCode = equipmentCode; }
}






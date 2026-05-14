package com.companya.labms.catalog;

import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "equipment_types")
public class EquipmentType extends BaseEntity {

    private String name;
    private String description;
    private String imageUrl;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

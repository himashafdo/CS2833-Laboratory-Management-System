package com.companya.labms.catalog;

/**
 * Lightweight DTO for the student-facing catalog grid.
 * One row per equipment NAME (not per individual unit).
 */
public class EquipmentTypeDto {
    private String name;
    private String description;
    private String imageUrl;
    private long totalUnits;
    private long availableUnits;

    public EquipmentTypeDto() {}

    public EquipmentTypeDto(String name, String description, String imageUrl,
                            long totalUnits, long availableUnits) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.totalUnits = totalUnits;
        this.availableUnits = availableUnits;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public long getTotalUnits() { return totalUnits; }
    public void setTotalUnits(long totalUnits) { this.totalUnits = totalUnits; }
    public long getAvailableUnits() { return availableUnits; }
    public void setAvailableUnits(long availableUnits) { this.availableUnits = availableUnits; }
}

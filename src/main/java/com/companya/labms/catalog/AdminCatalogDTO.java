package com.companya.labms.catalog;

import java.util.List;

public class AdminCatalogDTO {

    // ── Equipment Type DTO ──
    public static class EquipmentTypeDTO {
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

    // ── Equipment DTO (includes serialNumber + equipmentTypeId) ──
    public static class EquipmentDTO {
        private String name;
        private String description;
        private String imageUrl;
        private String status;
        private int quantity;
        private String serialNumber;
        private Long equipmentTypeId;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
        public Long getEquipmentTypeId() { return equipmentTypeId; }
        public void setEquipmentTypeId(Long equipmentTypeId) { this.equipmentTypeId = equipmentTypeId; }
        private String equipmentCode;
public String getEquipmentCode() { return equipmentCode; }
public void setEquipmentCode(String equipmentCode) { this.equipmentCode = equipmentCode; }
    }

    // ── Lab DTO ──
    public static class LabDTO {
        private String labName;
        private String location;
        private String status;

        public String getLabName() { return labName; }
        public void setLabName(String labName) { this.labName = labName; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // ── Stats DTO ──
    public static class StatsDTO {
        private long totalEquipment;
        private long availableEquipment;
        private long totalLabs;
        private long availableLabs;
        private long totalEquipmentTypes;

        public StatsDTO(long totalEquipment, long availableEquipment,
                        long totalLabs, long availableLabs, long totalEquipmentTypes) {
            this.totalEquipment = totalEquipment;
            this.availableEquipment = availableEquipment;
            this.totalLabs = totalLabs;
            this.availableLabs = availableLabs;
            this.totalEquipmentTypes = totalEquipmentTypes;
        }

        public long getTotalEquipment() { return totalEquipment; }
        public long getAvailableEquipment() { return availableEquipment; }
        public long getTotalLabs() { return totalLabs; }
        public long getAvailableLabs() { return availableLabs; }
        public long getTotalEquipmentTypes() { return totalEquipmentTypes; }
    }

    // ── Lab Equipment Assign DTO ──
    public static class LabEquipmentAssignDTO {
        private int quantity;
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // ── Lab With Equipment DTO ──
    public static class LabWithEquipmentDTO {
        private Long id;
        private String labName;
        private String location;
        private String status;
        private List<EquipmentSummary> equipment;

        public LabWithEquipmentDTO(Lab lab, List<EquipmentSummary> equipment) {
            this.id = lab.getId();
            this.labName = lab.getLabName();
            this.location = lab.getLocation();
            this.status = lab.getStatus().name();
            this.equipment = equipment;
        }

        public Long getId() { return id; }
        public String getLabName() { return labName; }
        public String getLocation() { return location; }
        public String getStatus() { return status; }
        public List<EquipmentSummary> getEquipment() { return equipment; }
    }

    // ── Equipment Summary (used inside lab detail) ──
    public static class EquipmentSummary {
        private Long equipmentId;
        private String name;
        private String status;
        private int quantity;

        public EquipmentSummary(Long equipmentId, String name, String status, int quantity) {
            this.equipmentId = equipmentId;
            this.name = name;
            this.status = status;
            this.quantity = quantity;
        }

        public Long getEquipmentId() { return equipmentId; }
        public String getName() { return name; }
        public String getStatus() { return status; }
        public int getQuantity() { return quantity; }
    }

    // ── Equipment Type with Count DTO ──
    public static class EquipmentTypeWithCountDTO {
        private Long id;
        private String name;
        private String description;
        private String imageUrl;
        private long equipmentCount;
        private long availableCount;

        public EquipmentTypeWithCountDTO(EquipmentType type, long total, long available) {
            this.id = type.getId();
            this.name = type.getName();
            this.description = type.getDescription();
            this.imageUrl = type.getImageUrl();
            this.equipmentCount = total;
            this.availableCount = available;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getImageUrl() { return imageUrl; }
        public long getEquipmentCount() { return equipmentCount; }
        public long getAvailableCount() { return availableCount; }
    }
}

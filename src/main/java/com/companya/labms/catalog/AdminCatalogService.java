package com.companya.labms.catalog;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.companya.labms.reservation.ReservationRepository;

@Service
public class AdminCatalogService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final LabRepository labRepository;
    private final LabEquipmentRepository labEquipmentRepository;
    private final ReservationRepository reservationRepository;

    public AdminCatalogService(EquipmentRepository equipmentRepository,
                           EquipmentTypeRepository equipmentTypeRepository,
                           LabRepository labRepository,
                           LabEquipmentRepository labEquipmentRepository,
                           ReservationRepository reservationRepository) {
    this.equipmentRepository = equipmentRepository;
    this.equipmentTypeRepository = equipmentTypeRepository;
    this.labRepository = labRepository;
    this.labEquipmentRepository = labEquipmentRepository;
    this.reservationRepository = reservationRepository;
}

    // ══════════════════════════════════════════
    // EQUIPMENT TYPES
    // ══════════════════════════════════════════

    public List<AdminCatalogDTO.EquipmentTypeWithCountDTO> getAllEquipmentTypes() {
        return equipmentTypeRepository.findAll().stream()
            .map(type -> {
                long total = equipmentRepository.findAll().stream()
                    .filter(e -> e.getEquipmentType() != null && e.getEquipmentType().getId().equals(type.getId()))
                    .count();
                long available = equipmentRepository.findAll().stream()
                    .filter(e -> e.getEquipmentType() != null && e.getEquipmentType().getId().equals(type.getId())
                        && e.getStatus() == Equipment.EquipmentStatus.AVAILABLE)
                    .count();
                return new AdminCatalogDTO.EquipmentTypeWithCountDTO(type, total, available);
            })
            .collect(Collectors.toList());
    }

    public EquipmentType addEquipmentType(AdminCatalogDTO.EquipmentTypeDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment type name cannot be empty");
        }
        
        EquipmentType type = new EquipmentType();
        type.setName(dto.getName().trim());
        type.setDescription(dto.getDescription());
        type.setImageUrl(dto.getImageUrl());
        return equipmentTypeRepository.save(type);
    }

    public EquipmentType updateEquipmentType(Long id, AdminCatalogDTO.EquipmentTypeDTO dto) {
        EquipmentType existing = equipmentTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Equipment type not found with ID: " + id));
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment type name cannot be empty");
        }
        existing.setName(dto.getName().trim());
        existing.setDescription(dto.getDescription());
        existing.setImageUrl(dto.getImageUrl());
        return equipmentTypeRepository.save(existing);
    }

    public void deleteEquipmentType(Long id) {
        if (!equipmentTypeRepository.existsById(id)) {
            throw new RuntimeException("Equipment type not found with ID: " + id);
        }
        // Null out equipmentType for any equipment using it
        equipmentRepository.findAll().stream()
            .filter(e -> e.getEquipmentType() != null && e.getEquipmentType().getId().equals(id))
            .forEach(e -> {
                e.setEquipmentType(null);
                equipmentRepository.save(e);
            });
            
        equipmentTypeRepository.deleteById(id);
    }

    // ══════════════════════════════════════════
    // EQUIPMENT
    // ══════════════════════════════════════════

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public List<Equipment> getEquipmentByType(Long typeId) {
        return equipmentRepository.findAll().stream()
            .filter(e -> e.getEquipmentType() != null && e.getEquipmentType().getId().equals(typeId))
            .collect(Collectors.toList());
    }

    public Equipment addEquipment(AdminCatalogDTO.EquipmentDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be empty");
        }
        if (dto.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        Equipment equipment = new Equipment();
        equipment.setName(dto.getName().trim());
        equipment.setDescription(dto.getDescription());
        equipment.setImageUrl(dto.getImageUrl());
        equipment.setSerialNumber(dto.getSerialNumber());
equipment.setQuantity(dto.getQuantity());
if (dto.getEquipmentCode() != null) equipment.setEquipmentCode(dto.getEquipmentCode());
        if (dto.getEquipmentTypeId() != null) {
            EquipmentType type = equipmentTypeRepository.findById(dto.getEquipmentTypeId())
                .orElseThrow(() -> new RuntimeException("Equipment type not found"));
            equipment.setEquipmentType(type);
        }

        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            try {
                equipment.setStatus(Equipment.EquipmentStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status. Use: AVAILABLE, RESERVED, MAINTENANCE, or UNAVAILABLE");
            }
        } else {
            equipment.setStatus(Equipment.EquipmentStatus.AVAILABLE);
        }

        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(Long id, AdminCatalogDTO.EquipmentDTO dto) {
        Equipment existing = equipmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Equipment not found with ID: " + id));

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be empty");
        }
        if (dto.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        existing.setName(dto.getName().trim());
        existing.setDescription(dto.getDescription());
        existing.setImageUrl(dto.getImageUrl());
        existing.setSerialNumber(dto.getSerialNumber());
existing.setQuantity(dto.getQuantity());
if (dto.getEquipmentCode() != null) existing.setEquipmentCode(dto.getEquipmentCode());

        if (dto.getEquipmentTypeId() != null) {
            EquipmentType type = equipmentTypeRepository.findById(dto.getEquipmentTypeId())
                .orElseThrow(() -> new RuntimeException("Equipment type not found"));
            existing.setEquipmentType(type);
        }

        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            try {
                existing.setStatus(Equipment.EquipmentStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status. Use: AVAILABLE, RESERVED, MAINTENANCE, or UNAVAILABLE");
            }
        }

        return equipmentRepository.save(existing);
    }

    public void deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new RuntimeException("Equipment not found with ID: " + id);
        }
        // Null out equipment on any reservations first
        reservationRepository.findByEquipmentId(id).forEach(r -> {
            r.setEquipment(null);
            reservationRepository.save(r);
        });
        // Remove lab assignments
        List<LabEquipment> assignments = labEquipmentRepository.findByEquipmentId(id);
        labEquipmentRepository.deleteAll(assignments);
        equipmentRepository.deleteById(id);
    }

    // ══════════════════════════════════════════
    // LABS
    // ══════════════════════════════════════════

    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }

    public Lab addLab(AdminCatalogDTO.LabDTO dto) {
        if (dto.getLabName() == null || dto.getLabName().trim().isEmpty()) {
            throw new IllegalArgumentException("Lab name cannot be empty");
        }
        Lab lab = new Lab();
        lab.setLabName(dto.getLabName().trim());
        lab.setLocation(dto.getLocation());
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            try {
                lab.setStatus(Lab.LabStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status. Use: AVAILABLE, IN_USE, or MAINTENANCE");
            }
        } else {
            lab.setStatus(Lab.LabStatus.AVAILABLE);
        }
        return labRepository.save(lab);
    }

    public Lab updateLab(Long id, AdminCatalogDTO.LabDTO dto) {
        Lab existing = labRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lab not found with ID: " + id));
        if (dto.getLabName() == null || dto.getLabName().trim().isEmpty()) {
            throw new IllegalArgumentException("Lab name cannot be empty");
        }
        existing.setLabName(dto.getLabName().trim());
        existing.setLocation(dto.getLocation());
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            try {
                existing.setStatus(Lab.LabStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status. Use: AVAILABLE, IN_USE, or MAINTENANCE");
            }
        }
        return labRepository.save(existing);
    }

    public void deleteLab(Long id) {
        if (!labRepository.existsById(id)) {
            throw new RuntimeException("Lab not found with ID: " + id);
        }
        List<LabEquipment> assignments = labEquipmentRepository.findByLabId(id);
        labEquipmentRepository.deleteAll(assignments);
        labRepository.deleteById(id);
    }

    // ══════════════════════════════════════════
    // ASSIGN / REMOVE EQUIPMENT FROM LAB
    // ══════════════════════════════════════════

    public LabEquipment assignEquipmentToLab(Long labId, Long equipmentId,
                                             AdminCatalogDTO.LabEquipmentAssignDTO dto) {
        Lab lab = labRepository.findById(labId)
            .orElseThrow(() -> new RuntimeException("Lab not found with ID: " + labId));
        Equipment equipment = equipmentRepository.findById(equipmentId)
            .orElseThrow(() -> new RuntimeException("Equipment not found with ID: " + equipmentId));

        if (dto.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        List<LabEquipment> existing = labEquipmentRepository.findByLabId(labId);
        for (LabEquipment le : existing) {
            if (le.getEquipment().getId().equals(equipmentId)) {
                le.setQuantity(dto.getQuantity());
                return labEquipmentRepository.save(le);
            }
        }

        LabEquipment labEquipment = new LabEquipment();
        labEquipment.setLab(lab);
        labEquipment.setEquipment(equipment);
        labEquipment.setQuantity(dto.getQuantity());
        return labEquipmentRepository.save(labEquipment);
    }

    public void removeEquipmentFromLab(Long labId, Long equipmentId) {
        List<LabEquipment> assignments = labEquipmentRepository.findByLabId(labId);
        LabEquipment toDelete = assignments.stream()
            .filter(le -> le.getEquipment().getId().equals(equipmentId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(
                "Equipment " + equipmentId + " is not assigned to lab " + labId));
        labEquipmentRepository.delete(toDelete);
    }

    public AdminCatalogDTO.LabWithEquipmentDTO getLabWithEquipment(Long labId) {
        Lab lab = labRepository.findById(labId)
            .orElseThrow(() -> new RuntimeException("Lab not found with ID: " + labId));
        List<LabEquipment> assignments = labEquipmentRepository.findByLabId(labId);
        List<AdminCatalogDTO.EquipmentSummary> equipmentList = assignments.stream()
            .map(le -> new AdminCatalogDTO.EquipmentSummary(
                le.getEquipment().getId(),
                le.getEquipment().getName(),
                le.getEquipment().getStatus().name(),
                le.getQuantity()))
            .collect(Collectors.toList());
        return new AdminCatalogDTO.LabWithEquipmentDTO(lab, equipmentList);
    }

    // ══════════════════════════════════════════
    // STATS
    // ══════════════════════════════════════════

    public AdminCatalogDTO.StatsDTO getStats() {
        long totalEquipment = equipmentRepository.count();
        long availableEquipment = equipmentRepository.findByStatus(Equipment.EquipmentStatus.AVAILABLE).size();
        long totalLabs = labRepository.count();
        long availableLabs = labRepository.findByStatus(Lab.LabStatus.AVAILABLE).size();
        long totalEquipmentTypes = equipmentTypeRepository.count();
        return new AdminCatalogDTO.StatsDTO(totalEquipment, availableEquipment, totalLabs, availableLabs, totalEquipmentTypes);
    }
}

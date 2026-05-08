package com.companya.labms.catalog;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CatalogService {

    private final EquipmentRepository equipmentRepository;
    private final LabRepository labRepository;
    private final LabEquipmentRepository labEquipmentRepository;

    public CatalogService(EquipmentRepository equipmentRepository,
                          LabRepository labRepository,
                          LabEquipmentRepository labEquipmentRepository) {
        this.equipmentRepository = equipmentRepository;
        this.labRepository = labRepository;
        this.labEquipmentRepository = labEquipmentRepository;
    }

    // ── EQUIPMENT (individual units) ──
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByStatus(Equipment.EquipmentStatus.AVAILABLE);
    }

    public List<Equipment> searchEquipment(String name) {
        return equipmentRepository.findByNameContainingIgnoreCase(name);
    }

    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
    }

    public Equipment addEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(Long id, Equipment updated) {
        Equipment existing = getEquipmentById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setImageUrl(updated.getImageUrl());
        existing.setStatus(updated.getStatus());
        return equipmentRepository.save(existing);
    }

    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    // ── EQUIPMENT TYPES (grouped by name for student catalog view) ──

    /**
     * Groups all equipment rows by name. Returns one DTO per unique name with
     * total + available unit counts. This is what students see on the catalog
     * page instead of 397 individual rows.
     */
    public List<EquipmentTypeDto> getEquipmentTypes() {
        List<Equipment> all = equipmentRepository.findAll();
        // LinkedHashMap to preserve insertion order, so first-seen description/image wins
        Map<String, EquipmentTypeDto> map = new LinkedHashMap<>();

        for (Equipment e : all) {
            EquipmentTypeDto dto = map.get(e.getName());
            if (dto == null) {
                dto = new EquipmentTypeDto(
                        e.getName(),
                        e.getDescription(),
                        e.getImageUrl(),
                        0L,
                        0L
                );
                map.put(e.getName(), dto);
            }
            dto.setTotalUnits(dto.getTotalUnits() + 1);
            if (e.getStatus() == Equipment.EquipmentStatus.AVAILABLE) {
                dto.setAvailableUnits(dto.getAvailableUnits() + 1);
            }
        }
        return new ArrayList<>(map.values());
    }

    /**
     * Search across grouped types by name fragment.
     */
    public List<EquipmentTypeDto> searchEquipmentTypes(String fragment) {
        return getEquipmentTypes().stream()
                .filter(t -> t.getName().toLowerCase().contains(fragment.toLowerCase()))
                .toList();
    }

    /**
     * Labs that have at least one unit of the given equipment name.
     * Modal Step 1 calls this.
     */
    public List<Lab> getLabsForEquipmentType(String name) {
        return labEquipmentRepository.findDistinctLabsByEquipmentName(name);
    }

    /**
     * AVAILABLE individual units of the given equipment name in the given lab.
     * Modal Step 2 calls this. Returns the actual Equipment rows so the
     * frontend gets id + equipment_code for each unit.
     */
    public List<Equipment> getAvailableUnitsInLab(String name, Long labId) {
        List<LabEquipment> links = labEquipmentRepository
                .findByEquipmentNameAndLabId(name, labId);
        List<Equipment> available = new ArrayList<>();
        for (LabEquipment link : links) {
            Equipment e = link.getEquipment();
            if (e != null && e.getStatus() == Equipment.EquipmentStatus.AVAILABLE) {
                available.add(e);
            }
        }
        return available;
    }

    // ── LABS ──
    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }

    public List<Lab> getAvailableLabs() {
        return labRepository.findByStatus(Lab.LabStatus.AVAILABLE);
    }

    public List<Lab> searchLabs(String name) {
        return labRepository.findByLabNameContainingIgnoreCase(name);
    }

    public Lab getLabById(Long id) {
        return labRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab not found"));
    }

    public Lab addLab(Lab lab) {
        return labRepository.save(lab);
    }

    public Lab updateLab(Long id, Lab updated) {
        Lab existing = getLabById(id);
        existing.setLabName(updated.getLabName());
        existing.setLocation(updated.getLocation());
        existing.setStatus(updated.getStatus());
        return labRepository.save(existing);
    }

    public void deleteLab(Long id) {
        labRepository.deleteById(id);
    }
}

package com.companya.labms.catalog;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CatalogService {

    private final EquipmentRepository equipmentRepository;
    private final LabRepository labRepository;

    public CatalogService(EquipmentRepository equipmentRepository,
                          LabRepository labRepository) {
        this.equipmentRepository = equipmentRepository;
        this.labRepository = labRepository;
    }

    // ── EQUIPMENT ──
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
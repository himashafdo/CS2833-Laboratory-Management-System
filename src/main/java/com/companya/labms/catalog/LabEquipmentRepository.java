package com.companya.labms.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LabEquipmentRepository extends JpaRepository<LabEquipment, Long> {
    List<LabEquipment> findByEquipmentId(Long equipmentId);
    List<LabEquipment> findByLabId(Long labId);
}
package com.companya.labms.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByStatus(Equipment.EquipmentStatus status);
    List<Equipment> findByNameContainingIgnoreCase(String name);
}
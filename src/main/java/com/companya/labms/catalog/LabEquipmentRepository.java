package com.companya.labms.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LabEquipmentRepository extends JpaRepository<LabEquipment, Long> {
    List<LabEquipment> findByEquipmentId(Long equipmentId);
    List<LabEquipment> findByLabId(Long labId);

    // NEW: distinct labs that hold at least one unit of an equipment name
    @Query("SELECT DISTINCT le.lab FROM LabEquipment le WHERE le.equipment.name = :name")
    List<Lab> findDistinctLabsByEquipmentName(@Param("name") String name);

    // NEW: every junction row for a given equipment name + lab
    // (one row per individual unit, since each unit is its own equipment row)
    @Query("SELECT le FROM LabEquipment le " +
           "WHERE le.equipment.name = :name AND le.lab.id = :labId")
    List<LabEquipment> findByEquipmentNameAndLabId(@Param("name") String name,
                                                    @Param("labId") Long labId);
}

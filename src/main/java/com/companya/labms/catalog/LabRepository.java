package com.companya.labms.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LabRepository extends JpaRepository<Lab, Long> {
    List<Lab> findByStatus(Lab.LabStatus status);
    List<Lab> findByLabNameContainingIgnoreCase(String name);
}
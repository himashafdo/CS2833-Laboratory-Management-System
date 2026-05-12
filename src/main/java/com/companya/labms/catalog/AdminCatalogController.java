package com.companya.labms.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/catalog")
@CrossOrigin(origins = "*")
public class AdminCatalogController {

    private final AdminCatalogService adminCatalogService;

    public AdminCatalogController(AdminCatalogService adminCatalogService) {
        this.adminCatalogService = adminCatalogService;
    }

    // ══════════════════════════════════════════
    // STATS
    // ══════════════════════════════════════════

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        try {
            return ResponseEntity.ok(adminCatalogService.getStats());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ══════════════════════════════════════════
    // EQUIPMENT TYPES
    // GET    /api/admin/catalog/equipment-types
    // POST   /api/admin/catalog/equipment-types
    // PUT    /api/admin/catalog/equipment-types/{id}
    // DELETE /api/admin/catalog/equipment-types/{id}
    // GET    /api/admin/catalog/equipment-types/{id}/equipment
    // ══════════════════════════════════════════

    @GetMapping("/equipment-types")
    public ResponseEntity<?> getAllEquipmentTypes() {
        try {
            return ResponseEntity.ok(adminCatalogService.getAllEquipmentTypes());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/equipment-types")
    public ResponseEntity<?> addEquipmentType(@RequestBody AdminCatalogDTO.EquipmentTypeDTO dto) {
        try {
            return ResponseEntity.ok(adminCatalogService.addEquipmentType(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/equipment-types/{id}")
    public ResponseEntity<?> updateEquipmentType(@PathVariable Long id,
                                                  @RequestBody AdminCatalogDTO.EquipmentTypeDTO dto) {
        try {
            return ResponseEntity.ok(adminCatalogService.updateEquipmentType(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/equipment-types/{id}")
    public ResponseEntity<?> deleteEquipmentType(@PathVariable Long id) {
        try {
            adminCatalogService.deleteEquipmentType(id);
            return ResponseEntity.ok(Map.of("message", "Equipment type deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/equipment-types/{typeId}/equipment")
    public ResponseEntity<?> getEquipmentByType(@PathVariable Long typeId) {
        try {
            return ResponseEntity.ok(adminCatalogService.getEquipmentByType(typeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ══════════════════════════════════════════
    // EQUIPMENT
    // ══════════════════════════════════════════

    @GetMapping("/equipment")
    public ResponseEntity<?> getAllEquipment() {
        try {
            return ResponseEntity.ok(adminCatalogService.getAllEquipment());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/equipment")
    public ResponseEntity<?> addEquipment(@RequestBody AdminCatalogDTO.EquipmentDTO dto) {
        try {
            return ResponseEntity.ok(adminCatalogService.addEquipment(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/equipment/{id}")
    public ResponseEntity<?> updateEquipment(@PathVariable Long id,
                                             @RequestBody AdminCatalogDTO.EquipmentDTO dto) {
        try {
            return ResponseEntity.ok(adminCatalogService.updateEquipment(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        try {
            adminCatalogService.deleteEquipment(id);
            return ResponseEntity.ok(Map.of("message", "Equipment deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // ══════════════════════════════════════════
    // LABS
    // ══════════════════════════════════════════

    @GetMapping("/labs")
    public ResponseEntity<?> getAllLabs() {
        try {
            return ResponseEntity.ok(adminCatalogService.getAllLabs());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/labs/{id}")
    public ResponseEntity<?> getLabWithEquipment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(adminCatalogService.getLabWithEquipment(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/labs")
    public ResponseEntity<?> addLab(@RequestBody AdminCatalogDTO.LabDTO dto) {
        try {
            return ResponseEntity.ok(adminCatalogService.addLab(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/labs/{id}")
    public ResponseEntity<?> updateLab(@PathVariable Long id,
                                       @RequestBody AdminCatalogDTO.LabDTO dto) {
        try {
            return ResponseEntity.ok(adminCatalogService.updateLab(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/labs/{id}")
    public ResponseEntity<?> deleteLab(@PathVariable Long id) {
        try {
            adminCatalogService.deleteLab(id);
            return ResponseEntity.ok(Map.of("message", "Lab deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/labs/{labId}/equipment/{equipId}")
    public ResponseEntity<?> assignEquipmentToLab(@PathVariable Long labId,
                                                   @PathVariable Long equipId,
                                                   @RequestBody AdminCatalogDTO.LabEquipmentAssignDTO dto) {
        try {
            LabEquipment result = adminCatalogService.assignEquipmentToLab(labId, equipId, dto);
            return ResponseEntity.ok(Map.of("message", "Equipment assigned successfully",
                                            "quantity", result.getQuantity()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/labs/{labId}/equipment/{equipId}")
    public ResponseEntity<?> removeEquipmentFromLab(@PathVariable Long labId,
                                                     @PathVariable Long equipId) {
        try {
            adminCatalogService.removeEquipmentFromLab(labId, equipId);
            return ResponseEntity.ok(Map.of("message", "Equipment removed from lab successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

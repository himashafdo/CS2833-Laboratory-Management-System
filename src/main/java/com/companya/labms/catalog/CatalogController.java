package com.companya.labms.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    // ── EQUIPMENT ENDPOINTS ──

    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> getAllEquipment(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(catalogService.searchEquipment(search));
        }
        if ("available".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(catalogService.getAvailableEquipment());
        }
        return ResponseEntity.ok(catalogService.getAllEquipment());
    }

    @GetMapping("/equipment/{id}")
    public ResponseEntity<?> getEquipment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(catalogService.getEquipmentById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/equipment")
    public ResponseEntity<?> addEquipment(@RequestBody Equipment equipment) {
        try {
            return ResponseEntity.ok(catalogService.addEquipment(equipment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/equipment/{id}")
    public ResponseEntity<?> updateEquipment(@PathVariable Long id,
                                              @RequestBody Equipment equipment) {
        try {
            return ResponseEntity.ok(catalogService.updateEquipment(id, equipment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/equipment/{id}/status")
    public ResponseEntity<?> updateEquipmentStatus(@PathVariable Long id,
                                                    @RequestBody Map<String, String> body) {
        try {
            Equipment equipment = catalogService.getEquipmentById(id);
            equipment.setStatus(Equipment.EquipmentStatus.valueOf(body.get("status").toUpperCase()));
            return ResponseEntity.ok(catalogService.addEquipment(equipment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        try {
            catalogService.deleteEquipment(id);
            return ResponseEntity.ok(Map.of("message", "Equipment deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── LAB ENDPOINTS ──

    @GetMapping("/labs")
    public ResponseEntity<List<Lab>> getAllLabs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(catalogService.searchLabs(search));
        }
        if ("available".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(catalogService.getAvailableLabs());
        }
        return ResponseEntity.ok(catalogService.getAllLabs());
    }

    @GetMapping("/labs/{id}")
    public ResponseEntity<?> getLab(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(catalogService.getLabById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/labs")
    public ResponseEntity<?> addLab(@RequestBody Lab lab) {
        try {
            return ResponseEntity.ok(catalogService.addLab(lab));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/labs/{id}")
    public ResponseEntity<?> updateLab(@PathVariable Long id,
                                        @RequestBody Lab lab) {
        try {
            return ResponseEntity.ok(catalogService.updateLab(id, lab));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/labs/{id}/status")
    public ResponseEntity<?> updateLabStatus(@PathVariable Long id,
                                              @RequestBody Map<String, String> body) {
        try {
            Lab lab = catalogService.getLabById(id);
            lab.setStatus(Lab.LabStatus.valueOf(body.get("status").toUpperCase()));
            return ResponseEntity.ok(catalogService.addLab(lab));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/labs/{id}")
    public ResponseEntity<?> deleteLab(@PathVariable Long id) {
        try {
            catalogService.deleteLab(id);
            return ResponseEntity.ok(Map.of("message", "Lab deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
package com.companya.labms.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class EquipmentRequestController {

    private final EquipmentRequestService service;

    public EquipmentRequestController(EquipmentRequestService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EquipmentRequest>> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    @GetMapping("/my")
    public ResponseEntity<List<EquipmentRequest>> getMyRequests(Principal principal) {
        return ResponseEntity.ok(service.getUserRequests(principal.getName()));
    }

    @PostMapping
    public ResponseEntity<EquipmentRequest> createRequest(Principal principal, @RequestBody EquipmentRequestDto dto) {
        return ResponseEntity.ok(service.createRequest(
                principal.getName(), 
                dto.getEquipmentDetails(), 
                dto.getJustification(), 
                dto.getQuantity(), 
                dto.getUrgencyLevel()
        ));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EquipmentRequest> updateStatus(@PathVariable Long id, @RequestParam EquipmentRequest.RequestStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        service.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}

class EquipmentRequestDto {
    private String equipmentDetails;
    private String justification;
    private Integer quantity;
    private EquipmentRequest.UrgencyLevel urgencyLevel;

    public String getEquipmentDetails() { return equipmentDetails; }
    public void setEquipmentDetails(String equipmentDetails) { this.equipmentDetails = equipmentDetails; }
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public EquipmentRequest.UrgencyLevel getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(EquipmentRequest.UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; }
}

package com.companya.labms.ticketing;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/tickets") // Admin-only path[cite: 4]
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignTicket(@RequestBody Map<String, Object> body) {
        try {
            Long issueId = Long.valueOf(body.get("issueId").toString());
            Long staffId = Long.valueOf(body.get("staffId").toString());
            String priority = body.getOrDefault("priority", "MEDIUM").toString();

            return ResponseEntity.ok(ticketService.assignIssueToStaff(issueId, staffId, priority));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<?> resolveTicket(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String resolution = body.get("resolution");
            String notes = body.get("notes");
            return ResponseEntity.ok(ticketService.resolveTicket(id, resolution, notes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
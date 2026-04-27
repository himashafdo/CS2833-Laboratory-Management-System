package com.companya.labms.ticketing;

import com.companya.labms.auth.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*")
public class IssueController {

    private final IssueService issueService;
    private final JwtUtil jwtUtil;

    public IssueController(IssueService issueService, JwtUtil jwtUtil) {
        this.issueService = issueService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Issue>> getAllIssues() {
        return ResponseEntity.ok(issueService.getAllIssues());
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyIssues(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String username = jwtUtil.extractUsername(authHeader.substring(7));
            return ResponseEntity.ok(issueService.getUserIssues(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createIssue(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> body) {
        try {
            String username = jwtUtil.extractUsername(authHeader.substring(7));
            String equipmentNumber = body.get("equipmentNumber") != null ?
        body.get("equipmentNumber").toString() : null;

            String issueType = body.get("issueType").toString();
            Long equipmentId = body.get("equipmentId") != null ?
                    Long.valueOf(body.get("equipmentId").toString()) : null;
            Long labId = body.get("labId") != null ?
                    Long.valueOf(body.get("labId").toString()) : null;
            String customType = body.get("customType") != null ?
                    body.get("customType").toString() : null;
            String title = body.get("title").toString();
            String description = body.get("description").toString();
            String priority = body.get("priority") != null ?
                    body.get("priority").toString() : "MEDIUM";
            LocalDate dateOccurred = body.get("dateOccurred") != null ?
                    LocalDate.parse(body.get("dateOccurred").toString()) : LocalDate.now();

            Issue issue = issueService.createIssue(username, issueType,
                    equipmentId, labId, customType, title,
                    description, priority, dateOccurred, null);
                    
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            Issue.IssueStatus status =
                    Issue.IssueStatus.valueOf(body.get("status").toUpperCase());
            return ResponseEntity.ok(issueService.updateStatus(id, status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
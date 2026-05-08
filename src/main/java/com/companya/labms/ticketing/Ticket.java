package com.companya.labms.ticketing;


import com.companya.labms.auth.User;
import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_tickets")
public class Ticket extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue originalIssue; // The student's report[cite: 3]

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo; // Admin or Lab Assistant[cite: 2, 3]

    private String internalNotes;

    private String resolutionDetails;

    private LocalDateTime assignmentDate;

    @Enumerated(EnumType.STRING)
    private TicketPriority adminPriority = TicketPriority.MEDIUM;

    public enum TicketPriority { LOW, MEDIUM, HIGH, URGENT }

    // Getters and Setters[cite: 3]
    public Issue getOriginalIssue() { return originalIssue; }
    public void setOriginalIssue(Issue originalIssue) { this.originalIssue = originalIssue; }
    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }
    public String getInternalNotes() { return internalNotes; }
    public void setInternalNotes(String internalNotes) { this.internalNotes = internalNotes; }
    public String getResolutionDetails() { return resolutionDetails; }
    public void setResolutionDetails(String resolutionDetails) { this.resolutionDetails = resolutionDetails; }
    public LocalDateTime getAssignmentDate() { return assignmentDate; }
    public void setAssignmentDate(LocalDateTime assignmentDate) { this.assignmentDate = assignmentDate; }
    public TicketPriority getAdminPriority() { return adminPriority; }
    public void setAdminPriority(TicketPriority adminPriority) { this.adminPriority = adminPriority; }
}

    

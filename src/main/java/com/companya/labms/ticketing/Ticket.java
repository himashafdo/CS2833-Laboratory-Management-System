package com.companya.labms.ticketing;

import com.companya.labms.auth.User;
import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

public class Ticket {
    
}
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
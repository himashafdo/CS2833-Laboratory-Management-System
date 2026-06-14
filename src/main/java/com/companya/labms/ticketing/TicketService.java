package com.companya.labms.ticketing;

import com.companya.labms.auth.User;
import com.companya.labms.auth.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import com.companya.labms.auth.JwtUtil;
import com.companya.labms.catalog.Equipment;
import com.companya.labms.catalog.EquipmentRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
private final EquipmentRepository equipmentRepository;

public TicketService(TicketRepository ticketRepository,
                     IssueRepository issueRepository,
                     UserRepository userRepository,
                     JwtUtil jwtUtil,
                     EquipmentRepository equipmentRepository) {
    this.ticketRepository    = ticketRepository;
    this.issueRepository     = issueRepository;
    this.userRepository      = userRepository;
    this.jwtUtil             = jwtUtil;
    this.equipmentRepository = equipmentRepository;
}

   

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    
    @Transactional
    public Ticket assignIssueToStaff(Long issueId, Long staffId, String priority) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff member not found"));

        // Update Issue status[cite: 2]
        issue.setStatus(Issue.IssueStatus.IN_PROGRESS);
        issueRepository.save(issue);

        Ticket ticket = ticketRepository.findByOriginalIssueId(issueId).orElse(new Ticket());
        ticket.setOriginalIssue(issue);
        ticket.setAssignedTo(staff);
        ticket.setAssignmentDate(LocalDateTime.now());
        ticket.setAdminPriority(Ticket.TicketPriority.valueOf(priority.toUpperCase()));

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket resolveTicket(Long ticketId, String resolution, String notes) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        ticket.setResolutionDetails(resolution);
        ticket.setInternalNotes(notes);
        
        // Update linked issue status to RESOLVED[cite: 2, 3]
        ticket.getOriginalIssue().setStatus(Issue.IssueStatus.RESOLVED);
        
        return ticketRepository.save(ticket);
    }
    @Transactional
public Ticket createInternalTicket(String title, String description, String priority, Long equipmentId, String token) {
    String username = jwtUtil.extractUsername(token);
    User createdBy = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Ticket ticket = new Ticket();
    ticket.setTitle(title);
    ticket.setDescription(description);
    ticket.setAssignmentDate(LocalDateTime.now());
    ticket.setAdminPriority(Ticket.TicketPriority.valueOf(priority.toUpperCase()));
    ticket.setAssignedTo(createdBy);
    ticket.setInternalNotes("Internal ticket raised by " + username);

    if (equipmentId != null) {
        Equipment equipment = equipmentRepository.findById(equipmentId).orElse(null);
        ticket.setEquipment(equipment);
    }

    return ticketRepository.save(ticket);
}
}
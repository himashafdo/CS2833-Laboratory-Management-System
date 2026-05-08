package com.companya.labms.ticketing;

import com.companya.labms.auth.User;
import com.companya.labms.auth.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, 
                         IssueRepository issueRepository, 
                         UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
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
}

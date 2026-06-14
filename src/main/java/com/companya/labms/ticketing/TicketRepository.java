package com.companya.labms.ticketing;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Find tickets assigned to a specific Lab Assistant or Admin[cite: 5]
    List<Ticket> findByAssignedToId(Long staffId);
    
    // Find the ticket associated with a specific student issue[cite: 5]
    Optional<Ticket> findByOriginalIssueId(Long issueId);
}
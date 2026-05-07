package com.companya.labms.ticketing;

import com.companya.labms.auth.User;
import com.companya.labms.auth.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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
    
}

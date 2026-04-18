package com.companya.labms.ticketing;

import com.companya.labms.auth.User;
import com.companya.labms.auth.UserRepository;
import com.companya.labms.catalog.Equipment;
import com.companya.labms.catalog.EquipmentRepository;
import com.companya.labms.catalog.Lab;
import com.companya.labms.catalog.LabRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final LabRepository labRepository;

    public IssueService(IssueRepository issueRepository,
                        UserRepository userRepository,
                        EquipmentRepository equipmentRepository,
                        LabRepository labRepository) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
        this.labRepository = labRepository;
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public List<Issue> getUserIssues(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return issueRepository.findByUserId(user.getId());
    }

    public Issue createIssue(String username, String issueType, Long equipmentId,
                              Long labId, String customType, String title,
                              String description, String priority,
                              LocalDate dateOccurred) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Issue issue = new Issue();
        issue.setUser(user);
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setDateOccurred(dateOccurred);
        issue.setIssueType(Issue.IssueType.valueOf(issueType.toUpperCase()));
        issue.setPriority(Issue.IssuePriority.valueOf(priority.toUpperCase()));

        if (issueType.equalsIgnoreCase("EQUIPMENT") && equipmentId != null) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("Equipment not found"));
            issue.setEquipment(equipment);
        } else if (issueType.equalsIgnoreCase("LAB") && labId != null) {
            Lab lab = labRepository.findById(labId)
                    .orElseThrow(() -> new RuntimeException("Lab not found"));
            issue.setLab(lab);
        } else if (issueType.equalsIgnoreCase("OTHER")) {
            issue.setCustomType(customType);
        }

        return issueRepository.save(issue);
    }

    public Issue updateStatus(Long id, Issue.IssueStatus status) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        issue.setStatus(status);
        return issueRepository.save(issue);
    }
}
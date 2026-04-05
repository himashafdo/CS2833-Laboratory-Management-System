package com.companya.labms.ticketing;

import com.companya.labms.auth.User;
import com.companya.labms.catalog.Equipment;
import com.companya.labms.catalog.Lab;
import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "issues")
public class Issue extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private IssueType issueType = IssueType.EQUIPMENT;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Lab lab;

    private String customType;

    private String title;

    private String description;

    private LocalDate dateOccurred;

    @Enumerated(EnumType.STRING)
    private IssueStatus status = IssueStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private IssuePriority priority = IssuePriority.MEDIUM;

    public enum IssueType { EQUIPMENT, LAB, OTHER }
    public enum IssueStatus { OPEN, IN_PROGRESS, RESOLVED, CLOSED }
    public enum IssuePriority { LOW, MEDIUM, HIGH }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public IssueType getIssueType() { return issueType; }
    public void setIssueType(IssueType issueType) { this.issueType = issueType; }
    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }
    public Lab getLab() { return lab; }
    public void setLab(Lab lab) { this.lab = lab; }
    public String getCustomType() { return customType; }
    public void setCustomType(String customType) { this.customType = customType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDateOccurred() { return dateOccurred; }
    public void setDateOccurred(LocalDate dateOccurred) { this.dateOccurred = dateOccurred; }
    public IssueStatus getStatus() { return status; }
    public void setStatus(IssueStatus status) { this.status = status; }
    public IssuePriority getPriority() { return priority; }
    public void setPriority(IssuePriority priority) { this.priority = priority; }
}
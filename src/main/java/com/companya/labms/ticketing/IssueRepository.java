package com.companya.labms.ticketing;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByUserId(Long userId);
    List<Issue> findByStatus(Issue.IssueStatus status);
    List<Issue> findByPriority(Issue.IssuePriority priority);
    List<Issue> findByIssueType(Issue.IssueType issueType);
}
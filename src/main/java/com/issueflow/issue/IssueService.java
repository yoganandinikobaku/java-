package com.issueflow.issue;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Transactional(readOnly = true)
    public List<Issue> findIssues(String query, IssueStatus status) {
        List<Issue> issues;
        if (query == null || query.isBlank()) {
            issues = issueRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
        } else {
            String term = query.trim();
            issues = issueRepository
                    .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByUpdatedAtDesc(
                            term, term);
        }
        if (status == null) {
            return issues;
        }
        return issues.stream().filter(issue -> issue.getStatus() == status).toList();
    }

    @Transactional(readOnly = true)
    public List<Issue> findAll() {
        return issueRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Issue getRequired(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id));
    }

    public Issue create(IssueForm form) {
        Issue issue = new Issue(
                form.getTitle().trim(),
                form.getDescription().trim(),
                form.getReporter().trim(),
                cleanOptional(form.getAssignee()),
                form.getStatus(),
                form.getPriority()
        );
        return issueRepository.save(issue);
    }

    public Issue update(Long id, IssueForm form) {
        Issue issue = getRequired(id);
        issue.update(
                form.getTitle().trim(),
                form.getDescription().trim(),
                form.getReporter().trim(),
                cleanOptional(form.getAssignee()),
                form.getStatus(),
                form.getPriority()
        );
        return issueRepository.save(issue);
    }

    public void delete(Long id) {
        issueRepository.delete(getRequired(id));
    }

    private String cleanOptional(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}

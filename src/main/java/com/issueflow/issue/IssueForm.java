package com.issueflow.issue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IssueForm {

    @NotBlank(message = "Enter a short issue title.")
    @Size(max = 120, message = "Title must be 120 characters or fewer.")
    private String title;

    @NotBlank(message = "Describe the issue.")
    @Size(max = 2000, message = "Description must be 2000 characters or fewer.")
    private String description;

    @NotBlank(message = "Enter the reporter's name.")
    @Size(max = 100, message = "Name must be 100 characters or fewer.")
    private String reporter;

    @Size(max = 100, message = "Name must be 100 characters or fewer.")
    private String assignee;

    @NotNull(message = "Choose a status.")
    private IssueStatus status = IssueStatus.OPEN;

    @NotNull(message = "Choose a priority.")
    private IssuePriority priority = IssuePriority.MEDIUM;

    public IssueForm() {
    }

    public IssueForm(Issue issue) {
        title = issue.getTitle();
        description = issue.getDescription();
        reporter = issue.getReporter();
        assignee = issue.getAssignee();
        status = issue.getStatus();
        priority = issue.getPriority();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }
}

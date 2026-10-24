package com.issueflow.issue;

public class IssueNotFoundException extends RuntimeException {

    public IssueNotFoundException(Long id) {
        super("Issue " + id + " was not found.");
    }
}

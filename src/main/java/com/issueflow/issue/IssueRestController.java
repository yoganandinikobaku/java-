package com.issueflow.issue;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    private final IssueService issueService;

    public IssueRestController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public List<Issue> list(@RequestParam(required = false) String q,
                            @RequestParam(required = false) IssueStatus status) {
        return issueService.findIssues(q, status);
    }

    @GetMapping("/{id}")
    public Issue get(@PathVariable Long id) {
        return issueService.getRequired(id);
    }

    @PostMapping
    public ResponseEntity<Issue> create(@Valid @RequestBody IssueForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(issueService.create(form));
    }

    @PutMapping("/{id}")
    public Issue update(@PathVariable Long id, @Valid @RequestBody IssueForm form) {
        return issueService.update(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        issueService.delete(id);
    }

    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(IssueNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Request validation failed.");
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
}

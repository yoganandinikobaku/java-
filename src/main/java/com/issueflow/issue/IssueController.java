package com.issueflow.issue;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) IssueStatus status,
                       Model model) {
        model.addAttribute("issues", issueService.findIssues(q, status));
        model.addAttribute("query", q == null ? "" : q);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", IssueStatus.values());
        model.addAttribute("totalCount", issueService.findAll().size());
        model.addAttribute("openCount", issueService.findIssues(null, IssueStatus.OPEN).size());
        model.addAttribute("progressCount", issueService.findIssues(null, IssueStatus.IN_PROGRESS).size());
        model.addAttribute("resolvedCount", issueService.findIssues(null, IssueStatus.RESOLVED).size());
        return "issues/index";
    }

    @GetMapping("/new")
    public String newIssue(Model model) {
        model.addAttribute("issueForm", new IssueForm());
        model.addAttribute("statuses", IssueStatus.values());
        model.addAttribute("priorities", IssuePriority.values());
        model.addAttribute("pageTitle", "Create issue");
        model.addAttribute("formAction", "/issues");
        return "issues/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("issueForm") IssueForm form,
                        BindingResult bindingResult, Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepareForm(model, "Create issue", "/issues");
            return "issues/form";
        }
        Issue created = issueService.create(form);
        redirectAttributes.addFlashAttribute("successMessage", "Issue created successfully.");
        return "redirect:/issues/" + created.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("issue", issueService.getRequired(id));
        return "issues/detail";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("issueForm", new IssueForm(issueService.getRequired(id)));
        model.addAttribute("statuses", IssueStatus.values());
        model.addAttribute("priorities", IssuePriority.values());
        model.addAttribute("pageTitle", "Edit issue");
        model.addAttribute("formAction", "/issues/" + id + "/edit");
        return "issues/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("issueForm") IssueForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        issueService.getRequired(id);
        if (bindingResult.hasErrors()) {
            prepareForm(model, "Edit issue", "/issues/" + id + "/edit");
            return "issues/form";
        }
        issueService.update(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Issue updated successfully.");
        return "redirect:/issues/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        issueService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Issue deleted.");
        return "redirect:/issues";
    }

    private void prepareForm(Model model, String title, String action) {
        model.addAttribute("statuses", IssueStatus.values());
        model.addAttribute("priorities", IssuePriority.values());
        model.addAttribute("pageTitle", title);
        model.addAttribute("formAction", action);
    }
}

@ControllerAdvice
class IssueExceptionHandler {

    @ExceptionHandler(IssueNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleIssueNotFound(IssueNotFoundException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error/404";
    }
}

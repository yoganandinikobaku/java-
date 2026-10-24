package com.issueflow.issue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueService issueService;

    @Test
    void createTrimsTextFields() {
        IssueForm form = new IssueForm();
        form.setTitle("  Login page error  ");
        form.setDescription("  Login returns a 500 response.  ");
        form.setReporter("  Nandini  ");
        form.setAssignee("  Developer  ");
        form.setStatus(IssueStatus.OPEN);
        form.setPriority(IssuePriority.HIGH);
        when(issueRepository.save(org.mockito.ArgumentMatchers.any(Issue.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        issueService.create(form);

        ArgumentCaptor<Issue> captor = ArgumentCaptor.forClass(Issue.class);
        verify(issueRepository).save(captor.capture());
        assertEquals("Login page error", captor.getValue().getTitle());
        assertEquals("Login returns a 500 response.", captor.getValue().getDescription());
        assertEquals("Nandini", captor.getValue().getReporter());
        assertEquals("Developer", captor.getValue().getAssignee());
        assertEquals(IssueStatus.OPEN, captor.getValue().getStatus());
        assertEquals(IssuePriority.HIGH, captor.getValue().getPriority());
    }
}

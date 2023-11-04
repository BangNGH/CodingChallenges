package com.example.coderlab.service;

import com.example.coderlab.entity.*;
import com.example.coderlab.repository.SubmissionRepository;
import com.example.coderlab.utils.SubmissionInfoSendDTO;
import com.example.coderlab.utils.TestCaseListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final AssignmentService assignmentService;
    private final AssesmentService assesmentService;
    private final TestCaseService testCaseService;

    public void save(Submission submission) {
        this.submissionRepository.save(submission);
    }

    public Optional<Submission> getSubmission(long submissionId) {
       return this.submissionRepository.findById(submissionId);
    }

    public void saveSubmissions(SubmissionInfoSendDTO submission_sent_form_client, UserEntity current_user) {
        Assignment foundChallenge = assignmentService.getAssignmentById(submission_sent_form_client.getAssignment_id());
        Submission submission = new Submission();
        submission.setLanguage(submission_sent_form_client.getLanguage());
        submission.setSource_code(submission_sent_form_client.getSourceCode());
        submission.setStudent(current_user);
        submission.setAssignment(foundChallenge);
        List<Assessment> assessments = new ArrayList<>();
        int my_score = 0;
        for (TestCaseListDTO testCaseListDTO :submission_sent_form_client.getTestCaseListDTOS()
             ) {
            Assessment assessment = new Assessment();
            assessment.setExecutionTime(testCaseListDTO.getExecutionTime());
            assessment.setMemoryUsed(testCaseListDTO.getMemory());
            assessment.setSubmission(submission);
            assessment.setMy_output(testCaseListDTO.getMy_output());
            if (testCaseListDTO.getIspassed()==true){
                assessment.setIspassed(true);
                List<TestCase> testCases_of_assignment = foundChallenge.getTestCases();
                Optional<TestCase> testCaseOptional = testCases_of_assignment.stream()
                        .filter(testCase -> testCase.getInput().equals(testCaseListDTO.getStdin()) && testCase.getExpectedOutput().equals(testCaseListDTO.getExpected_output()))
                        .findFirst();
                if (testCaseOptional.isPresent()) {
                    my_score += testCaseOptional.get().getScore();
                } else {
                    System.out.println("Test Case not found for input: " + testCaseListDTO.getStdin() + " and output: " + testCaseListDTO.getExpected_output());
                }
            }else assessment.setIspassed(false);
            assessments.add(assessment);
        }
        if (my_score==100){
            submission.setIs_success(true);
        }else submission.setIs_success(false);
        submission.setAssessments(assessments);
        Submission savedSubmission = submissionRepository.save(submission);
        for (Assessment assessment :savedSubmission.getAssessments()
        ) {
            assessment.setSubmission(savedSubmission);
            assesmentService.save(assessment);
        }

    }
}

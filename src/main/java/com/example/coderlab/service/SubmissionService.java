package com.example.coderlab.service;

import com.example.coderlab.entity.*;
import com.example.coderlab.repository.SubmissionRepository;
import com.example.coderlab.dto.SubmissionInfoSendDTO;
import com.example.coderlab.dto.TestCaseListDTO;
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
    private final SolutionCheckService solutionCheckService;
    private final UserServices userServices;
    private final LanguageService languageService;

    public void save(Submission submission) {
        this.submissionRepository.save(submission);
    }

    public Optional<Submission> getSubmission(long submissionId) {
       return this.submissionRepository.findById(submissionId);
    }

    public Long saveSubmissions(SubmissionInfoSendDTO submission_sent_form_client, UserEntity current_user) {
        Assignment foundChallenge = assignmentService.getAssignmentById(submission_sent_form_client.getAssignment_id());
        List<TestCase> testCases_of_assignment = foundChallenge.getTestCases();
        Submission submission = new Submission();

        //languageID_id
        String languageValue = submission_sent_form_client.getLanguage();
        Optional<Language> foundLanguage = languageService.findLanguageByValue(languageValue);
        if (foundLanguage.isPresent()) {
            submission.setLanguage(foundLanguage.get());
        }else submission.setLanguage(null);

        submission.setSource_code(submission_sent_form_client.getSourceCode());
        submission.setStudent(current_user);
        submission.setAssignment(foundChallenge);
        List<Assessment> assessments = new ArrayList<>();
        int my_score = 0;
        int totalScore = foundChallenge.getTestCases().stream()
                .mapToInt(TestCase::getScore)
                .sum();

        for (TestCaseListDTO testCaseListDTO :submission_sent_form_client.getTestCaseListDTOS()
             ) {
            Assessment assessment = new Assessment();

            assessment.setExecutionTime(testCaseListDTO.getExecutionTime());
            assessment.setMemoryUsed(testCaseListDTO.getMemory());
            assessment.setSubmission(submission);
            assessment.setMy_output(testCaseListDTO.getMy_output());

            Optional<TestCase> testCaseOptional = testCases_of_assignment.stream()
                    .filter(testCase -> testCase.getInput().equals(testCaseListDTO.getStdin()) && testCase.getExpectedOutput().equals(testCaseListDTO.getExpected_output()))
                    .findFirst();
            if (testCaseOptional.isPresent()) {
                assessment.setTestCase(testCaseOptional.get());
            } else {
                System.out.println("Test Case not found for input: " + testCaseListDTO.getStdin() + " and output: " + testCaseListDTO.getExpected_output());
            }
            if (testCaseListDTO.getIspassed()==true){
                my_score += testCaseOptional.get().getScore();
                assessment.setIspassed(true);
            }else assessment.setIspassed(false);
            assessments.add(assessment);
        }

        if (my_score==totalScore){
            submission.setIs_success(true);
        }else submission.setIs_success(false);
        submission.setAssessments(assessments);
        Boolean is_unlocked_solution = solutionCheckService.isUnlocked(current_user, foundChallenge);
        if (is_unlocked_solution == true){
            submission.setTotal_score(0);
        }else submission.setTotal_score(my_score);

        Submission savedSubmission = submissionRepository.save(submission);
        current_user.getSubmissions().add(savedSubmission);
        userServices.save(current_user);
        for (Assessment assessment :savedSubmission.getAssessments()
        ) {
            assessment.setSubmission(savedSubmission);
            assesmentService.save(assessment);
        }
        return submission.getId();
    }

    public List<Submission> getSubmissions(Long userID, Long assignmentID) {
        return this.submissionRepository.getSubmissions(userID, assignmentID);
    }

    public List<Object[]> rankByAssignment() {
        return submissionRepository.rankByAssignment();
    }

    public Submission saveSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }
}

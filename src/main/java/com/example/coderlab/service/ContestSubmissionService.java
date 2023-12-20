package com.example.coderlab.service;

import com.example.coderlab.dto.SubmissionKitInfoSendDTO;
import com.example.coderlab.entity.*;
import com.example.coderlab.repository.ContestSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContestSubmissionService {
    @Autowired
    private ContestSubmissionRepository contestSubmissionRepository;
    @Autowired
    private ContestService contestService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private UserServices userServices;


    public Boolean save(SubmissionKitInfoSendDTO submissionsId, UserEntity currentUser) {
        Optional<Contest> foundContestByID = contestService.findById(Long.valueOf(submissionsId.getAssignment_kit_id()));
        Boolean is_passed = true;
        if (foundContestByID.isPresent()) {
            Contest contest = foundContestByID.get();

            ContestSubmission contestSubmission = new ContestSubmission();
            contestSubmission.setContest(contest);
            contestSubmission.setUser_submitted(currentUser);
            ContestSubmission savedContestSubmission = contestSubmissionRepository.save(contestSubmission);


            List<Submission> submissions = new ArrayList<>();
            List<String> submissions_id = submissionsId.getSubmissions_id_ofAssignment();
            System.out.println(submissions_id);
            for (String submission_id : submissions_id
            ) {
                Submission found_submission = submissionService.getSubmission(Long.parseLong(submission_id)).get();
                found_submission.setContestSubmission(savedContestSubmission);
                if (found_submission.getIs_success() == false) {
                    is_passed = false;
                }
                submissionService.saveSubmission(found_submission);
                submissions.add(found_submission);
            }

            savedContestSubmission.setIs_success(is_passed);
            savedContestSubmission.setSubmissions(submissions);
            contestSubmissionRepository.save(savedContestSubmission);

            currentUser.getContestSubmissions().add(savedContestSubmission);
            userServices.save(currentUser);
        }
        return is_passed;
    }

    public List<ContestSubmission> getContestSubmissions(Contest foundContest, UserEntity currentUser) {
        return contestSubmissionRepository.getContestSubmissions(foundContest, currentUser);
    }

    public List<ContestSubmission> getCorrectContestAnswer(Contest foundContest, UserEntity currentUser) {
        return contestSubmissionRepository.getCorrectContestAnswer(foundContest, currentUser);
    }
}

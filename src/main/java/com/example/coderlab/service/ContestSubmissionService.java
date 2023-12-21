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
            Integer numberAnswerCorrect = 0;
            Integer contest_score = 0;
            Contest contest = foundContestByID.get();
            ContestSubmission contestSubmission = new ContestSubmission();
            contestSubmission.setContest(contest);
            contestSubmission.setUser_submitted(currentUser);
            contestSubmission.setTotalTime(Integer.valueOf(submissionsId.getSubmissions_id_ofQuiz().get(0).toString()));
            ContestSubmission savedContestSubmission = contestSubmissionRepository.save(contestSubmission);


            List<Submission> submissions = new ArrayList<>();
            List<String> submissions_id = submissionsId.getSubmissions_id_ofAssignment();
            for (String submission_id : submissions_id
            ) {
                Submission found_submission = submissionService.getSubmission(Long.parseLong(submission_id)).get();
                found_submission.setContestSubmission(savedContestSubmission);
                if (found_submission.getIs_success() == false) {
                    is_passed = false;
                }else {
                    numberAnswerCorrect++;
                }
                contest_score+=found_submission.getTotal_score();
                submissionService.saveSubmission(found_submission);
                submissions.add(found_submission);
            }

            savedContestSubmission.setIs_success(is_passed);
            savedContestSubmission.setTotalScore(contest_score);
            savedContestSubmission.setCorrectAnswer(numberAnswerCorrect);
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

    public ContestSubmission getContestSubmission(Contest foundContest, UserEntity currentUser) {
        return contestSubmissionRepository.getContestSubmission(foundContest, currentUser);
    }

    public List<Object[]> rankContest(Contest foundContest) {
        return contestSubmissionRepository.contestRank(foundContest);
    }
}

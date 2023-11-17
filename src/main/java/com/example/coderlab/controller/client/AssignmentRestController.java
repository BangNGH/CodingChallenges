package com.example.coderlab.controller.client;

import com.example.coderlab.entity.*;
import com.example.coderlab.service.*;
import com.example.coderlab.utils.SubmissionInfoSendDTO;
import com.example.coderlab.utils.SubmissionKitInfoSendDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/submissions")
public class AssignmentRestController {
    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private AssignmentKitSubmissionService submissionKitService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserServices userServices;

    @PostMapping("/add")
    public String handleSubmission(@RequestBody SubmissionInfoSendDTO submission, Principal principal) {
        try {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
             Long id_submission = submissionService.saveSubmissions(submission, current_user);
             return id_submission.toString();
        } catch (Exception e) {
            return "1";
        }
    }

    @PostMapping("/end-contest")
    public String endContest(@RequestBody SubmissionKitInfoSendDTO submissions_id, Principal principal) {
        try {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
           Boolean status = submissionKitService.saveSubmissions(submissions_id, current_user);
            if (status){
                return "passed";
            }
            return "failed";
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }
    }

    @GetMapping("/comment")
    @ResponseBody
    public Comment commentPost(@RequestParam("comment") String comment, @RequestParam("assignment_id") long assignmentID, Principal principal) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentID);
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        return commentService.save(comment, assignment, current_user);
    }

    @GetMapping("/get-assignment-info")
    @ResponseBody
    public String getAssignmentInfo(@RequestParam("assignment_id") Long assignment_id) throws JsonProcessingException {
        Assignment assignment = assignmentService.getAssignmentById(assignment_id);
        List<TestCase> sampleTestCase = assignment.getTestCases().stream().filter(testCase -> testCase.isMarkSampleTestCase() == true).toList();
        ObjectMapper objectMapper = new ObjectMapper();
        String sampleTestCasesJson = objectMapper.writeValueAsString(sampleTestCase);
        String allTestCasesJson = objectMapper.writeValueAsString(assignment.getTestCases());
        String time_limit = objectMapper.writeValueAsString(assignment.getTimeLimit());
        String memory_limit = objectMapper.writeValueAsString(assignment.getMemoryLimit());

        return "{ \"sampleTestCases\":" + sampleTestCasesJson + ", \"allTestCases\":" + allTestCasesJson + ", \"time_limit\":" + time_limit + ", \"memory_limit\":" + memory_limit + " }";
    }
}

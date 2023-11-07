package com.example.coderlab.controller.client;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.Comment;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.service.*;
import com.example.coderlab.utils.SubmissionInfoSendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/submissions")
public class AssignmentRestController {
    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserServices userServices;

    @PostMapping("/add")
    public ResponseEntity<?> handleSubmission(@RequestBody SubmissionInfoSendDTO submission, Principal principal) {
        try {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            submissionService.saveSubmissions(submission, current_user);
            return ResponseEntity.ok("Submissions added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when adding submissions: " + e.getMessage());
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


}

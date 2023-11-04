package com.example.coderlab.controller.client;

import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.service.AssesmentService;
import com.example.coderlab.service.SubmissionService;
import com.example.coderlab.service.UserServices;
import com.example.coderlab.utils.SubmissionInfoSendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/submissions")
public class AssignmentRestController {
    @Autowired
    private SubmissionService submissionService;

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


}

package com.example.coderlab.controller.teacher;

import com.example.coderlab.dto.ContestDTO;
import com.example.coderlab.dto.SubmissionInfoSendDTO;
import com.example.coderlab.dto.SubmissionKitInfoSendDTO;
import com.example.coderlab.entity.*;
import com.example.coderlab.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/teacher")
public class TeacherRestController {
    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private AssignmentKitSubmissionService submissionKitService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private UserServices userServices;

    @Autowired
    private SolutionCheckService solutionCheckService;
    @Autowired
    private QuizService quizService;
    @GetMapping("/remove-assignment")
    public String postTest(@RequestParam(value = "contest_id") String contest_id, @RequestParam(value = "assignment_id") String assignment_id, Principal principal) {
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        Optional<Contest> foundByIDContest = contestService.findById(Long.valueOf(contest_id));
        Assignment foundAssignment = assignmentService.getAssignmentById(Long.valueOf(assignment_id));
        if (foundByIDContest.isPresent()&&foundAssignment!=null) {
            Contest foundContest = foundByIDContest.get();
            foundContest.getAssignments().remove(foundAssignment);
            String lastedUpdate = "(ID:"+current_user.getId()+"): "+ current_user.getFullName();
            foundContest.setLatestUserUpdate(lastedUpdate);
            contestService.saveContest(foundContest);
            return "success";

        }
        return "error";
    }

}

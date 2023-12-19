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
    @PostMapping("/contest/edit")
    public String add(@RequestBody ContestDTO contestDTO, Principal principal, RedirectAttributes redirectAttributes) {
        Optional<Contest> foundContest = contestService.findById(contestDTO.getContest_id());
        List<Assignment> assignmentSetssignments = new ArrayList<>();
        List<Question> questionSet =new ArrayList<>();
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        if (foundContest.isPresent()) {
            Contest contest = foundContest.get();
            if (contestDTO.getIs_random()){
                for (Long quiz_id:contestDTO.getSelected_questions_id()
                ) {
                    Question foundQuestion = quizService.findQuestionById(String.valueOf(quiz_id));
                    if (foundQuestion != null) {
                        questionSet.add(foundQuestion);
                    }
                }
                for (Long assignment_id:contestDTO.getSelected_assignments_id()
                ) {
                    Assignment foundAssignment = assignmentService.getAssignmentById(assignment_id);
                    if (foundAssignment!=null) {
                        assignmentSetssignments.add(foundAssignment);
                    }
                }
            }else{
                assignmentSetssignments = assignmentService.getRandomAssignments(contestDTO.getNo_assignment());
                questionSet = quizService.getRandomQuizs(contestDTO.getNo_quiz());
            }
            contest.setQuizQuestions(questionSet);
            contest.setAssignments(assignmentSetssignments);
            String lastedUpdate = "(ID:"+current_user.getId()+"): "+ current_user.getFullName();
            contest.setLatestUserUpdate(lastedUpdate);
            contestService.saveContest(contest);
            redirectAttributes.addFlashAttribute("message", "Chỉnh sửa cuộc thi: "+contest.getId()+" thành công");
        }else {
            redirectAttributes.addFlashAttribute("message", "Xảy ra lỗi");
        }
        return "redirect:/teacher/contest";
    }
}

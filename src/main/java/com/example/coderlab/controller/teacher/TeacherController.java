package com.example.coderlab.controller.teacher;

import com.example.coderlab.dto.AssignmentLeaderBoardDTO;
import com.example.coderlab.entity.*;
import com.example.coderlab.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher/contest")
public class TeacherController {
    private final ContestService contestService;
    private final AssignmentService assignmentService;
    private final QuizService quizService;
    private final SubmissionService submissionService;

    @GetMapping("")
    public String myContest(Model model) {
        model.addAttribute("contests", contestService.getContests());
        return "teacher/contest/index";
    }

    @GetMapping("/add-assignment/{id}")
    public String addAssignmentToContest(@PathVariable("id") Long contestID, Model model) {
        Optional<Contest> foundContest = contestService.findById(contestID);
        if (foundContest.isPresent()) {
            model.addAttribute("assignments", assignmentService.getContestAssignment());
            model.addAttribute("contest", foundContest.get());
            ;
            model.addAttribute("contests", contestService.getContests());
            model.addAttribute("assignments", assignmentService.getContestAssignment());
            model.addAttribute("quiz_list", quizService.getAllQuizzs());
            return "teacher/contest/testadd";
        }
        return "redirect:/teacher/contest";
    }


}

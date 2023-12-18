package com.example.coderlab.controller.teacher;

import com.example.coderlab.dto.AssignmentLeaderBoardDTO;
import com.example.coderlab.entity.Contest;
import com.example.coderlab.entity.Role;
import com.example.coderlab.entity.UserEntity;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/add-assignment")
    public String add(@RequestParam("contest_id") Long contest_id, @RequestParam("no_assignment") Integer no_assignment, @RequestParam("no_quiz") Integer no_quiz, @RequestParam(value = "isRandomQuestion", required = false) Boolean isRandomQuestion
            , @RequestParam(value = "assignments[]", required = false) List<Long> assignments, @RequestParam(value = "quizList[]", required = false) List<Long> quizList
            , Principal principal, RedirectAttributes redirectAttributes) {

        System.out.println(contest_id);
        System.out.println(no_assignment);
        System.out.println(no_quiz);
        System.out.println(isRandomQuestion);
        if (isRandomQuestion==null){
            System.out.println("Random Question");
        }else{
            System.out.println(assignments);
            System.out.println(quizList);
        }

        return "redirect:/teacher/contest";
    }
}

package com.example.coderlab.controller.teacher;

import com.example.coderlab.dto.AssignmentLeaderBoardDTO;
import com.example.coderlab.entity.*;
import com.example.coderlab.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher/contest")
public class TeacherController {
    private final ContestService contestService;
    private final AssignmentService assignmentService;
    private final QuizService quizService;
    private final SubmissionService submissionService;

    private final UserServices userServices;
    @GetMapping("")
    public String myContest(Model model, Principal principal) {
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        model.addAttribute("contests", current_user.getContests());
        return "teacher/index";
    }

    @GetMapping("/add-assignment/{id}")
    public String addAssignmentToContest(@PathVariable("id") Long contestID, Model model) {
        Optional<Contest> foundContest = contestService.findById(contestID);
        if (foundContest.isPresent()) {
            Contest existsContest =foundContest.get();
            if (existsContest.getAssignments().size()==0){
                model.addAttribute("assignments", assignmentService.getContestAssignment());
            }else {
                List<Assignment> allAssignment =  assignmentService.getContestAssignment();
                List<Assignment> chosenAssignment = existsContest.getAssignments();
                List<Assignment> unchosenAssignment = allAssignment.stream ()
                        .filter (element -> !chosenAssignment.contains (element))
                        .collect (Collectors.toList ());
                model.addAttribute("assignments", unchosenAssignment);
                model.addAttribute("chosenAssignment", chosenAssignment);
            }
            model.addAttribute("contest",existsContest);
            model.addAttribute("contests", contestService.getContests());
            return "/teacher/add";
        }
        return "redirect:/teacher/contest";
    }
    @PostMapping("/edit")
    public String postTest(@ModelAttribute("contest")Contest contest, Principal principal, RedirectAttributes redirectAttributes, @RequestParam(value = "isRandomQuestion", required = false) Boolean isRandomQuestion) {
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        Optional<Contest> foundByIDContest = contestService.findById(contest.getId());
        if (foundByIDContest.isPresent()) {
            Contest foundContest = foundByIDContest.get();
            if (isRandomQuestion==null){
                foundContest.setIsRandomAssignment(true);
            }else{
                foundContest.setIsRandomAssignment(false);
            }
            String lastedUpdate = "(ID:"+current_user.getId()+"): "+ current_user.getFullName();
            foundContest.setLatestUserUpdate(lastedUpdate);
            foundContest.setNumberOfAssignment(contest.getNumberOfAssignment());

            List<Assignment> existsListAssignment=  foundContest.getAssignments();
            List<Assignment> newAssignment= contest.getAssignments();
            List<Assignment> unmergedAssignment = newAssignment.stream ()
                    .filter (element -> !existsListAssignment.contains (element))
                    .collect (Collectors.toList ());
            existsListAssignment.addAll (unmergedAssignment);
            foundContest.setAssignments(existsListAssignment);
            foundContest.setLatestUserUpdate(lastedUpdate);
           contestService.saveContest(foundContest);

            redirectAttributes.addFlashAttribute("message", "Chỉnh sửa cuộc thi: "+contest.getId()+" thành công");

        }else redirectAttributes.addFlashAttribute("message", "Xảy ra lỗi!!!");
            return "redirect:/teacher/contest";
    }


}

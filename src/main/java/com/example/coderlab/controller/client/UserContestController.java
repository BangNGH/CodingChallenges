package com.example.coderlab.controller.client;

import com.example.coderlab.entity.*;
import com.example.coderlab.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Controller
@RequestMapping("/contest")
@RequiredArgsConstructor
public class UserContestController {
    private final AssignmentKitService assignmentKitService;
    private final AssignmentKitSubmissionService assignmentKitSubmissionService;
    private final UserServices userServices;
    private final LanguageService languageService;
    private final AssignmentService assignmentService;
    private final ContestSubmissionService contestSubmissionService;
    private final ContestService contestService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        clearSession(session);
        model.addAttribute("contests", contestService.getContests());
        return "client/contest/index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, Principal principal, HttpSession session) {
        clearSession(session);
        Optional<Contest> foundContestByID = contestService.findById(id);
        if (foundContestByID.isPresent()) {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            Contest foundContest = foundContestByID.get();

            List<ContestSubmission> findContestSubmissions = contestSubmissionService.getContestSubmissions(foundContest, current_user);
            if (!findContestSubmissions.isEmpty()) {
                List<ContestSubmission> contestSubmissions = contestSubmissionService.getCorrectContestAnswer(foundContest, current_user);
               if (contestSubmissions.size()==0) {
                   model.addAttribute("correctAnswer", 0);
                   model.addAttribute("myTestScore", 0);
               }else {
                   Integer correctAnswer = contestSubmissions.size();
                   Integer myTestScore = 0;
                   for (ContestSubmission contestSubmission : contestSubmissions
                   ) {
                       for (Submission submission : contestSubmission.getSubmissions()
                       ) {
                           myTestScore+=submission.getTotal_score();
                       }
                       contestSubmission.getSubmissions();
                   }
                   model.addAttribute("correctAnswer", correctAnswer);
                   model.addAttribute("myTestScore", myTestScore);
               }
                model.addAttribute("already_tested", true);

            } else {
                model.addAttribute("already_tested", false);
            }

            model.addAttribute("contest", foundContest);

            return "client/contest/details";
        }
        redirectAttributes.addFlashAttribute("message", "Not found certify with ID: " + id);
        return "redirect:/contest";

    }

    @GetMapping("/join/{id}")
    public String enrollTest(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        Optional<Contest> foundContestByID = contestService.findById(id);
        if (foundContestByID.isPresent()) {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            Contest foundContest = foundContestByID.get();
            List<Assignment> assignments = new ArrayList<>();
            if (foundContest.getIsRandomAssignment()) {
                assignments = assignmentService.getRandomAssignments(foundContest.getNumberOfAssignment());
            } else {
                assignments = foundContest.getAssignments();
            }
            List<Long> idAssignments = new ArrayList<Long>();
            for (Assignment a : assignments) {
                idAssignments.add(a.getId());
            }
            model.addAttribute("idAssignments", idAssignments);
            model.addAttribute("assignments", assignments);
            model.addAttribute("contest", foundContest);
            model.addAttribute("user_id", current_user.getId());
            model.addAttribute("languages", languageService.getAllLanguages());
            return "client/contest/join_contest";
        }
        redirectAttributes.addFlashAttribute("message", "Not found certify with ID: " + id);
        return "redirect:/contest";
    }

    private Boolean clearSession(HttpSession session) {
        if (session != null) {
            session.removeAttribute("editorContent");
            session.removeAttribute("mode");
            session.removeAttribute("option");
            session.removeAttribute("language_name");
            session.removeAttribute("current_tab_id");
            System.out.println("Session cleared successfully!");
            return true;
        }
        return false;
    }

}

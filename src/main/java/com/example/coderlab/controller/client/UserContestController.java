package com.example.coderlab.controller.client;

import com.example.coderlab.dto.AssignmentLeaderBoardDTO;
import com.example.coderlab.dto.ContestLeaderBoardDTO;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        model.addAttribute("contests", contestService.getContests());
        return "client/contest/index";
    }
    @GetMapping("/rank/{id}")
    public String rank(@PathVariable("id") Long id, Model model) {
        Optional<Contest> foundContestByID = contestService.findById(id);
        Contest foundContest = foundContestByID.get();
        List<Object[]> rankByAssignment = contestSubmissionService.rankContest(foundContest);
        List<ContestLeaderBoardDTO> rankList = new ArrayList<ContestLeaderBoardDTO>();
        for (Object[] result : rankByAssignment) {
            ContestLeaderBoardDTO contestLeaderBoardDTO = new ContestLeaderBoardDTO();
            Long rank = (Long) result[0];
            contestLeaderBoardDTO.setRank(rank);
            Long userId = (Long) result[1];
            Optional<UserEntity> foundUser = userServices.findById(userId);
            if (foundUser!=null) {
                contestLeaderBoardDTO.setUser(foundUser.get());
                Integer correct_answer = (Integer) result[2];
                contestLeaderBoardDTO.setCorrect_answer(correct_answer);
                Integer total_score = (Integer) result[3];
                contestLeaderBoardDTO.setTotal_score(total_score);
                Integer total_time = (Integer) result[4];
                contestLeaderBoardDTO.setTotal_time(total_time);
            }
            rankList.add(contestLeaderBoardDTO);
        }

        model.addAttribute("rankList", rankList);
        model.addAttribute("numberOfAssignment", foundContest.getNumberOfAssignment());
        model.addAttribute("title", foundContest.getContestName());
        return "client/contest/rank";
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
                ContestSubmission contestSubmission = contestSubmissionService.getContestSubmission(foundContest, current_user);
                   Integer myTestScore = 0;
                   for (Submission submission : contestSubmission.getSubmissions()
                   ) {
                       myTestScore+=submission.getTotal_score();
                   }
                   model.addAttribute("correctAnswer", contestSubmission.getCorrectAnswer());
                   model.addAttribute("myTestScore", myTestScore);
                   model.addAttribute("totalTime", contestSubmission.getTotalTime());

                model.addAttribute("already_tested", true);
            } else {
                model.addAttribute("already_tested", false);
            }
            if (foundContest.getAssignments().size() > 0) {
                model.addAttribute("assignments", foundContest.getAssignments());
            }else {
                model.addAttribute("assignments", assignmentService.getRandomAssignments(foundContest.getNumberOfAssignment()));
            }

                LocalDateTime end = LocalDateTime.ofInstant(foundContest.getEndTime().toInstant(), ZoneId.systemDefault());
                LocalDateTime now = LocalDateTime.now();
                if (now.isAfter(end)) {
                    foundContest.setActive(false);
                   contestService.saveContest(foundContest);
                    model.addAttribute("contestEnded", true);
                }else   model.addAttribute("contestEnded", false);



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

            List<ContestSubmission> findContestSubmissions = contestSubmissionService.getContestSubmissions(foundContest, current_user);
            if (!findContestSubmissions.isEmpty()) {
                return "redirect:/contest/details/"+foundContest.getId();
            }else {
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
            return "client/contest/join_contest";  }
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

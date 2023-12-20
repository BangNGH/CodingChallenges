package com.example.coderlab.controller.client;

import com.example.coderlab.entity.*;
import com.example.coderlab.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Controller
@RequestMapping("/skills-verification")
@RequiredArgsConstructor
public class CertifyController {
    private final AssignmentKitService assignmentKitService;
    private final AssignmentKitSubmissionService assignmentKitSubmissionService;
    private final UserServices userServices;
    private final LanguageService languageService;
    private final AssignmentService assignmentService;
    private final QuizService quizService;
    public UserEntity getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userServices.findByEmail(email).orElseThrow();
    }
    @GetMapping
    public String index(Model model, HttpSession session){
        clearSession(session);
        model.addAttribute("UserID", getUser().getId());
        model.addAttribute("assignment_kits", assignmentKitService.getAllAssignmentsKit());
        return "client/certify/index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, Principal principal, HttpSession session) {
        clearSession(session);
        Optional<AssignmentKit> found_kit_by_id = assignmentKitService.findById(id);
        if (found_kit_by_id.isPresent()) {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            List<AssignmentKitSubmission> found_assignments_kit = assignmentKitSubmissionService.getByAssignmentKit_User_Id(found_kit_by_id.get(), current_user).stream().sorted(Comparator.comparing(AssignmentKitSubmission::getSubmitted_at)).toList();

            if (!found_assignments_kit.isEmpty()){
                model.addAttribute("already_tested", true);
                Boolean is_passed = found_assignments_kit.get(found_assignments_kit.size()-1).getIs_success();
                LocalDate last_attempt_at = LocalDate.from(found_assignments_kit.get(found_assignments_kit.size()-1).getSubmitted_at());
                if (is_passed == false){
                    LocalDate now = LocalDate.now();
                    long daysBetween = DAYS.between(last_attempt_at, now);
                    if (daysBetween>=30){
                        model.addAttribute("retake", true);
                    }else{
                        model.addAttribute("retake", false);
                    }
                    model.addAttribute("is_passed", false);
                    model.addAttribute("last_attempt", last_attempt_at);
                }else {
                    model.addAttribute("is_passed", true);
                    model.addAttribute("last_attempt", last_attempt_at);
                }
            } else {
                model.addAttribute("already_tested", false);
            }

            model.addAttribute("assignment_kit", found_kit_by_id.get());
            return "client/certify/details";
        }
        redirectAttributes.addFlashAttribute("message", "Not found certify with ID: " + id);
        return "redirect:/skills-verification";

    }
    @GetMapping("/test/{id}")
    public String enrollTest(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        Optional<AssignmentKit> found_kit_by_id = assignmentKitService.findById(id);
        if (found_kit_by_id.isPresent()) {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            AssignmentKit foundAssignmentKit = found_kit_by_id.get();
            List<AssignmentKitSubmission> found_assignments_kit = assignmentKitSubmissionService.getByAssignmentKit_User_Id(foundAssignmentKit, current_user);

            //trường hợp user này chưa làm certify
            if (found_assignments_kit.isEmpty()){
                List<Assignment> randomAssignments = assignmentService.getRandomAssignments(foundAssignmentKit.getNumberOfAssignment(),foundAssignmentKit.getLanguage(), foundAssignmentKit.getLevel());
                List<Question> randomQuizs = quizService.getRandomQuizs(foundAssignmentKit.getNumberOfQuiz(),foundAssignmentKit.getLanguage(), foundAssignmentKit.getLevel());

                List<Long> idAssignments=new ArrayList<Long>();
                for (Assignment a : randomAssignments){
                    idAssignments.add(a.getId());
                }
                List<Long> idQuizs=new ArrayList<Long>();
                for (Question a : randomQuizs){
                    idQuizs.add(a.getId());
                }
                model.addAttribute("assignment_kit", found_kit_by_id.get());
                model.addAttribute("randomAssignments", randomAssignments);
                model.addAttribute("idAssignments", idAssignments);
                model.addAttribute("idQuizs", idQuizs);
                model.addAttribute("randomQuiz", randomQuizs);
                model.addAttribute("user_id", current_user.getId());
                model.addAttribute("languages",languageService.getAllLanguages());
                return "client/certify/certify";
            }
            Boolean is_passed = found_assignments_kit.get(found_assignments_kit.size()-1).getIs_success();
            if (is_passed) {
                return "redirect:/skills-verification/details/"+id;
            }else {
                LocalDate last_attempt_at = LocalDate.from(found_assignments_kit.get(found_assignments_kit.size()-1).getSubmitted_at());
                LocalDate now = LocalDate.now();
                long daysBetween = DAYS.between(last_attempt_at, now);
                if (daysBetween>=30) {
                    List<Assignment> randomAssignments = assignmentService.getRandomAssignments(foundAssignmentKit.getNumberOfAssignment(),foundAssignmentKit.getLanguage(), foundAssignmentKit.getLevel());
                    List<Question> randomQuizs = quizService.getRandomQuizs(foundAssignmentKit.getNumberOfQuiz(),foundAssignmentKit.getLanguage(), foundAssignmentKit.getLevel());

                    List<Long> idAssignments=new ArrayList<Long>();
                    for (Assignment a : randomAssignments){
                        idAssignments.add(a.getId());
                    }
                    List<Long> idQuizs=new ArrayList<Long>();
                    for (Question a : randomQuizs){
                        idQuizs.add(a.getId());
                    }
                    model.addAttribute("assignment_kit", found_kit_by_id.get());
                    model.addAttribute("randomAssignments", randomAssignments);
                    model.addAttribute("idAssignments", idAssignments);
                    model.addAttribute("idQuizs", idQuizs);
                    model.addAttribute("randomQuiz", randomQuizs);
                    model.addAttribute("user_id", current_user.getId());
                    model.addAttribute("languages",languageService.getAllLanguages());
                    return "client/certify/certify";

                }else {
                    return "redirect:/skills-verification/details/"+id;

                }
            }
        }
        redirectAttributes.addFlashAttribute("message", "Not found certify with ID: " + id);
        return "redirect:/skills-verification";
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

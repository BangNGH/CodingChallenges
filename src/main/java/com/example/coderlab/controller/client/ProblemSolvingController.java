package com.example.coderlab.controller.client;

import com.example.coderlab.entity.*;
import com.example.coderlab.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/problemSolving")
public class ProblemSolvingController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private UserServices userServices;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private SolutionCheckService solutionCheckService;
    @Autowired
    private LanguageService languageService;

    public UserEntity getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
         Assignment assignment = new Assignment();
        return userServices.findByEmail(email).orElseThrow();
    }
    @GetMapping()
    public String index(Model model){
        model.addAttribute("UserID", getUser().getId());
        return "client/problem/index";
    }
    @GetMapping("/topic/{id}")
    public String practiceByTopic(Model model, @PathVariable("id") Long id){
        model.addAttribute("UserID", getUser().getId());
        model.addAttribute("topicID", id);
        model.addAttribute("language", languageService.findByLanguageID(id).orElseThrow());
        return "client/problem/topic";
    }

//    @GetMapping("/page/{pageNo}")
//    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model, String byTopicID){
//        int pageSize = 10;
//        Page<Assignment> page;
//        if (byTopicID != null){
//            page = assignmentService.findPaginatedByTopic(pageNo, pageSize, byTopicID);
//        }else {
//            page = assignmentService.findPaginated(pageNo, pageSize);
//        }
//        List<Assignment> listAssignment = page.getContent();
//
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//
//        model.addAttribute("assignments", listAssignment);
//        return "client/problem/index";
//    }
    @GetMapping("/{challengeID}")
    public String practice(@PathVariable("challengeID")Long challengeID, Model model, Principal principal) throws JsonProcessingException {
        Assignment foundChallenge = assignmentService.getAssignmentById(challengeID);
        List<TestCase> sampleTestCase = foundChallenge.getTestCases().stream().filter(testCase -> testCase.isMarkSampleTestCase() == true).toList();
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        List<Submission> submissions = submissionService.getSubmissions(current_user.getId(), foundChallenge.getId());

        if (!submissions.isEmpty()){
            model.addAttribute("submissions", submissions.stream().filter(i->i.getAssignment_kit_submission()==null).sorted(Comparator.comparing(Submission::getSubmittedAt, Comparator.reverseOrder())).collect(Collectors.toList()));
        }else  model.addAttribute("submissions", false);
        model.addAttribute("test_cases_json", new ObjectMapper().writeValueAsString(sampleTestCase));
        model.addAttribute("all_test_cases_json", new ObjectMapper().writeValueAsString(foundChallenge.getTestCases()));
        model.addAttribute("challenge", foundChallenge);
        List<Comment> comments = foundChallenge.getComments().stream().sorted(Comparator.comparing(Comment::getCommented_at).reversed()).toList();
        model.addAttribute("comments", comments);
        model.addAttribute("languages", languageService.getAllLanguages());
        model.addAttribute("unlocked", solutionCheckService.isUnlocked(current_user, foundChallenge));
        return "client/problem/practice";
    }
}

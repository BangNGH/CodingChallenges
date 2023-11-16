package com.example.coderlab.controller.client;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.Comment;
import com.example.coderlab.entity.TestCase;
import com.example.coderlab.service.AssignmentService;
import com.example.coderlab.service.RegistrationRequest;
import com.example.coderlab.service.RoleService;
import com.example.coderlab.service.TestCaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor    
public class HomeController {
    private final RoleService roleService;
    private final AssignmentService assignmentService;
    @GetMapping("/")
    public String home(){
        return "client/home/index";
    }

    @GetMapping("/login-access-account")
    public String loginAccessAccount(Model model){
        roleService.addRole();
        model.addAttribute("dev", "For Developers");
        model.addAttribute("devInfo", "We are the market–leading technical interview platform to identify and hire developers with the right skills.");
        model.addAttribute("bus", "For Companies");
        model.addAttribute("busInfo", "We are the market–leading technical interview platform to identify and hire developers with the right skills.");
        model.addAttribute("btnLg", true);
        return "access-account";
    }
    @GetMapping("/register-access-account")
    public String registerAccessAccount(Model model){
        roleService.addRole();
        model.addAttribute("dev", "I'm here to practice and prepare");
        model.addAttribute("devInfo", "Solve problems and learn new skills");
        model.addAttribute("bus", "I'm here to hire tech talent");
        model.addAttribute("busInfo", "Evaluate tech skill at scale");
        model.addAttribute("btnRg", true);
        return "access-account";
    }
    @GetMapping("/login")
    public String login(){
        roleService.addRole();
        return "sign-in";
    }
//    @GetMapping("/practice/{challengeID}")
//    public String practice(@PathVariable("challengeID")Long challengeID, Model model) throws JsonProcessingException {
//        Assignment foundChallenge = assignmentService.getAssignmentById(challengeID);
//        List<TestCase> sampleTestCase = foundChallenge.getTestCases().stream().filter(testCase -> testCase.isMarkSampleTestCase() == true).toList();
//        model.addAttribute("test_cases_json", new ObjectMapper().writeValueAsString(sampleTestCase));
//        model.addAttribute("all_test_cases_json", new ObjectMapper().writeValueAsString(foundChallenge.getTestCases()));
//        model.addAttribute("challenge", foundChallenge);
//
//        List<Comment> comments = foundChallenge.getComments().stream().sorted(Comparator.comparing(Comment::getCommented_at).reversed()).toList();
//        model.addAttribute("comments", comments);
//        return "client/practice/practice";
//    }
    @GetMapping("/company-register")
    public String companyRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("COMPANY", "", "", ""));
        return "sign-up";
    }


}

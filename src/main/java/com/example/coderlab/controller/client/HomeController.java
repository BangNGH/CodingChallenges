package com.example.coderlab.controller.client;

import com.example.coderlab.dto.AssignmentLeaderBoardDTO;
import com.example.coderlab.entity.Language;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor    
public class HomeController {
    private final RoleService roleService;
    private final LanguageService languageService;
    private final UserServices userServices;
    private final SubmissionService submissionService;

    @GetMapping("/prepare")
    public String prepare(Model model){
        model.addAttribute("languages", languageService.getAllLanguages());
        return "client/prepare/index";
    }
    @GetMapping("/rank")
    public String rank(Model model){
        List<Object[]> rankByAssignment = submissionService.rankByAssignment();
        List<AssignmentLeaderBoardDTO> rankList = new ArrayList<AssignmentLeaderBoardDTO>();
        for (Object[] result : rankByAssignment) {
            AssignmentLeaderBoardDTO assignmentLeaderBoardDTO = new AssignmentLeaderBoardDTO();
            Long rank = (Long) result[0];
            assignmentLeaderBoardDTO.setRank(rank);
            Long userId = (Long) result[1];
            Optional<UserEntity> foundUser = userServices.findById(userId);
            if (foundUser!=null) {
                assignmentLeaderBoardDTO.setUser(foundUser.get());
                Long solved_assignments = (Long) result[2];
                assignmentLeaderBoardDTO.setSolvedAssignments(solved_assignments);
            }
            rankList.add(assignmentLeaderBoardDTO);
        }

        model.addAttribute("rankList", rankList);
        return "client/rank/index";
    }
    @EventListener(ApplicationReadyEvent.class)
    public void addDataWhenStartUp() {
        roleService.addRole();
        languageService.addLanguage();
    }

    @GetMapping("/")
    public String home(){
        return "client/home/index";
    }

    @GetMapping("/login-access-account")
    public String loginAccessAccount(Model model){
        model.addAttribute("dev", "For Developers");
        model.addAttribute("devInfo", "We are the market–leading technical interview platform to identify and hire developers with the right skills.");
        model.addAttribute("bus", "For Companies");
        model.addAttribute("busInfo", "We are the market–leading technical interview platform to identify and hire developers with the right skills.");
        model.addAttribute("btnLg", true);
        return "access-account";
    }
    @GetMapping("/register-access-account")
    public String registerAccessAccount(Model model){
        model.addAttribute("dev", "I'm here to practice and prepare");
        model.addAttribute("devInfo", "Solve problems and learn new skills");
        model.addAttribute("bus", "I'm here to hire tech talent");
        model.addAttribute("busInfo", "Evaluate tech skill at scale");
        model.addAttribute("btnRg", true);
        return "access-account";
    }
    @GetMapping("/login")
    public String login(){
        return "sign-in";
    }
    @GetMapping("/company-register")
    public String companyRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("COMPANY", "", "", ""));
        return "sign-up";
    }


}

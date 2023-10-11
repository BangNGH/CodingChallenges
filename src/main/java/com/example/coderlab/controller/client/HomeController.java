package com.example.coderlab.controller.client;

import com.example.coderlab.service.RegistrationRequest;
import com.example.coderlab.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final RoleService roleService;
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
    @GetMapping("/company-register")
    public String companyRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("COMPANY", "", "", ""));
        return "sign-up";
    }

}

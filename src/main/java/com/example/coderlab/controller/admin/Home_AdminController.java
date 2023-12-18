package com.example.coderlab.controller.admin;

import com.example.coderlab.service.AssignmentService;
import com.example.coderlab.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class Home_AdminController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private UserServices userServices;

    @GetMapping
    public String home(){
        return "admin/home/index";
    }
    @GetMapping("/assignments-bank")
    public String assignmentBank(Model model){
        model.addAttribute("assignments", assignmentService.getContestAssignment());
        return "admin/assignment_bank/index";
    }
}

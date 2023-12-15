package com.example.coderlab.controller.admin;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.entity.Question;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/admin/assignment-kit")
public class Assignment_KitController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentKitService assignmentKitService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private LevelService levelService;
    @Autowired
    private UserServices userServices;
    @GetMapping()
    public String index(Model model){
        if(model.containsAttribute("message")){
            model.addAttribute("message", model.getAttribute("message"));
        }
        model.addAttribute("assignment_kits", assignmentKitService.getAllAssignmentsKit());
        return "admin/assignment_kit/index";
    }
    @GetMapping("/add")
    public String addKit(Model model){
        model.addAttribute("assignment_kit", new AssignmentKit());
        model.addAttribute("languages",languageService.getAllLanguages());
        model.addAttribute("levels", levelService.getListLevel());
        return "admin/assignment_kit/add";
    }
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("assignment_kit") AssignmentKit assignmentKit, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "admin/assignment_kit/add";
        }
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        assignmentKit.setUser_added(current_user);
        assignmentKitService.save(assignmentKit);
        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/admin/assignment-kit";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Assignment assignment = assignmentService.getAssignmentById(id);
        model.addAttribute("assignment",assignment);
        model.addAttribute("number_testcase", assignment.getTestCases().size() - 1);
        var test = assignment.getTestCases();
        return "admin/assignment/edit";
    }

}

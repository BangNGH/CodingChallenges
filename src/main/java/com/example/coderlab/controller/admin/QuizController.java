package com.example.coderlab.controller.admin;

import com.example.coderlab.entity.Question;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/quiz")
public class QuizController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private LevelService levelService;
    @Autowired
    private UserServices userServices;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("questions", quizService.getAllQuizzs());
        return "admin/quiz/index";
    }
    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("languages",languageService.getAllLanguages());
        model.addAttribute("levels", levelService.getListLevel());
        model.addAttribute("quiz", new Question());
        return "admin/quiz/add";
    }
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("quiz")Question question, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) {
            return "admin/quiz/add";
        }
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        question.setCreatedUser(current_user);
        quizService.save(question);
        return "redirect:/admin/quiz";
    }
}

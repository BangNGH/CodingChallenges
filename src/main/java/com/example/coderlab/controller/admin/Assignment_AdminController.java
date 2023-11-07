package com.example.coderlab.controller.admin;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.TestCase;
import com.example.coderlab.service.AssignmentService;
import com.example.coderlab.service.TestCaseService;
import com.example.coderlab.service.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/assignment")
public class Assignment_AdminController {
    @Autowired
    private AssignmentService assignmentService;

    @GetMapping()
    public String index(Model model){
        if(model.containsAttribute("message")){
            model.addAttribute("message", model.getAttribute("message"));
        }
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        return "admin/assignment/index";
    }
    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("assignment", new Assignment());
        return "admin/assignment/add";
    }
    @PostMapping("/add")
    public String addAssignment(@RequestParam("title") String title,@RequestParam("description") String description,
                                @RequestParam("timeLimit") int timeLimit, @RequestParam("memoryLimit") int memoryLimit,
                                RedirectAttributes redirectAttributes, @RequestParam("TSName[]") List<String> TestCaseNames,
                                @RequestParam("TSScore[]") List<Integer> TestCaseScores, @RequestParam("TSInput[]") List<String> TestCaseInputs,
                                @RequestParam("TSOutput[]") List<String> TestCaseOutputs, @RequestParam(value = "check[]", required = false) List<Boolean> MaskSamples) throws IOException {

        assignmentService.addAssignment(title, description,timeLimit,memoryLimit,TestCaseNames, TestCaseScores, TestCaseInputs, TestCaseOutputs, MaskSamples);
        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/admin/assignment";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Assignment assignment = assignmentService.getAssignmentById(id);
        model.addAttribute("assignment",assignment);
        model.addAttribute("number_testcase", assignment.getTestCases().size() - 1);
        var test = assignment.getTestCases();
        return "admin/assignment/edit";
    }
    @PostMapping("/edit")
    public String edit(@ModelAttribute("assignment") Assignment updateAssignment,
                       @RequestParam("description") String description,
                       @RequestParam("TSName[]") List<String> TestCaseNames,
                       @RequestParam("TSScore[]") List<Integer> TestCaseScores, @RequestParam("TSInput[]") List<String> TestCaseInputs,
                       @RequestParam("TSOutput[]") List<String> TestCaseOutputs, @RequestParam(value = "check[]", required = false) List<Boolean> MaskSamples,
                       RedirectAttributes redirectAttributes) {

        assignmentService.updateAssignment(updateAssignment, description,TestCaseNames, TestCaseScores, TestCaseInputs, TestCaseOutputs, MaskSamples);
        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/admin/assignment";
    }
    @GetMapping("/test")
    public String test(){
        return "admin/assignment/test";
    }
}

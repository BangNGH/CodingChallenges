package com.example.coderlab.controller.admin;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.entity.Level;
import com.example.coderlab.service.AssignmentKitService;
import com.example.coderlab.service.AssignmentService;
import com.example.coderlab.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/assignment")
public class Assignment_AdminController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentKitService assignmentKitService;
    @Autowired
    private LevelService levelService;
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
        model.addAttribute("levels", levelService.getListLevel());
        return "admin/assignment/add";
    }
    @PostMapping("/add")
    public String addAssignment(@RequestParam("title") String title,@RequestParam("description") String description,
                                @RequestParam("level") Long level,
                                @RequestParam(value = "timeLimit", required=false) Integer timeLimit, @RequestParam(value = "memoryLimit", required=false) Integer memoryLimit,
                                RedirectAttributes redirectAttributes, @RequestParam("TSName[]") List<String> TestCaseNames,
                                @RequestParam("TSScore[]") List<Integer> TestCaseScores, @RequestParam("TSInput[]") List<String> TestCaseInputs,
                                @RequestParam("TSOutput[]") List<String> TestCaseOutputs, @RequestParam(value = "check[]", required = false) List<Boolean> MaskSamples) throws IOException {

        assignmentService.addAssignment(title, description,timeLimit,memoryLimit,TestCaseNames, TestCaseScores, TestCaseInputs, TestCaseOutputs, MaskSamples, level);
        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/admin/assignment";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Assignment assignment = assignmentService.getAssignmentById(id);
        model.addAttribute("assignment",assignment);
        model.addAttribute("number_testcase", assignment.getTestCases().size() - 1);
        model.addAttribute("levels", levelService.getListLevel());
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

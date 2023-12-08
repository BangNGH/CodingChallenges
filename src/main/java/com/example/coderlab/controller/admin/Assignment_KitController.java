package com.example.coderlab.controller.admin;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.service.AssignmentKitService;
import com.example.coderlab.service.AssignmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/admin/assignment-kit")
public class Assignment_KitController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentKitService assignmentKitService;

    @GetMapping()
    public String index(Model model){
        if(model.containsAttribute("message")){
            model.addAttribute("message", model.getAttribute("message"));
        }
        model.addAttribute("assignment_kits", assignmentKitService.getAllAssignmentsKit());
        return "admin/assignment_kit/index";
    }
    @GetMapping("/add")
    public String addKit(Model model) throws JsonProcessingException {
        model.addAttribute("assignment_kit", new AssignmentKit());
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        model.addAttribute("assignments_json", new ObjectMapper().writeValueAsString(assignmentService.getAllAssignments()));
        return "admin/assignment_kit/add";
    }
    @PostMapping("/add")
    public String addAssignment(@RequestParam("title") String title, @RequestParam("time") Integer time, @RequestParam("description") String description,
                                @RequestParam(value = "multi_coding_select[]", required = false) List<Long> assignments_kit, RedirectAttributes redirectAttributes) throws IOException {
        assignmentKitService.addKit(title, (time),description,assignments_kit);
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

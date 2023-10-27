package com.example.coderlab.controller.admin;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.TestCase;
import com.example.coderlab.service.AssignmentService;
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
    @Autowired
    private UserServices userServices;
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
                                RedirectAttributes redirectAttributes, @RequestParam("TSName[]") List<String> TestCaseNames,
                                @RequestParam("TSScore[]") List<Integer> TestCaseScores, @RequestParam("TSInput[]") List<String> TestCaseInputs,
                                @RequestParam("TSOutput[]") List<String> TestCaseOutputs, @RequestParam(value = "check[]", required = false) List<Boolean> MaskSamples) throws IOException {
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);

        List<TestCase> testCases = new ArrayList<>();
        for (int i = 0; i < TestCaseNames.size(); i++) {
            TestCase testCase = new TestCase();
            testCase.setName(TestCaseNames.get(i));
            testCase.setScore(TestCaseScores.get(i));
            testCase.setInput(TestCaseInputs.get(i));
            testCase.setExpectedOutput(TestCaseOutputs.get(i));
            if (MaskSamples != null && i < MaskSamples.size()) {
                testCase.setMarkSampleTestCase(MaskSamples.get(i));
            } else {
                testCase.setMarkSampleTestCase(false);
            }
            testCases.add(testCase);
        }
        assignment.setTestCases(testCases);
        assignmentService.addAssignment(assignment);
        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/admin/assignment";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Assignment assignment = assignmentService.getAssignmentById(id);
        model.addAttribute(assignment);
        return "admin/assignment/edit";
    }
    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("assignment") Assignment updateAssignment,
                       BindingResult bindingResult, Model model,
                       @RequestParam("img") MultipartFile multipartFile,
                       RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "_error",
                        error.getDefaultMessage());
            }
            model.addAttribute("assignment",updateAssignment);
            return "admin/assignment/edit";
        }
        assignmentService.updateAssignment(updateAssignment);
        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/admin/assignment";
    }
    @GetMapping("/test")
    public String test(){
        return "admin/assignment/test";
    }
}

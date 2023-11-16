package com.example.coderlab.controller.client;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.Comment;
import com.example.coderlab.entity.TestCase;
import com.example.coderlab.service.AssignmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("problemSolving")
public class ProblemSolvingController {
    @Autowired
    private AssignmentService assignmentService;
    @GetMapping()
    public String index(Model model){

        return findPaginated(1, model);
    }
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model){
        int pageSize = 10;
        Page<Assignment> page = assignmentService.findPaginated(pageNo, pageSize);
        List<Assignment> listAssignment = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("assignments", listAssignment);
        return "client/problem/index";
    }
    @GetMapping("/{challengeID}")
    public String practice(@PathVariable("challengeID")Long challengeID, Model model) throws JsonProcessingException {
        Assignment foundChallenge = assignmentService.getAssignmentById(challengeID);
        List<TestCase> sampleTestCase = foundChallenge.getTestCases().stream().filter(testCase -> testCase.isMarkSampleTestCase() == true).toList();
        model.addAttribute("test_cases_json", new ObjectMapper().writeValueAsString(sampleTestCase));
        model.addAttribute("all_test_cases_json", new ObjectMapper().writeValueAsString(foundChallenge.getTestCases()));
        model.addAttribute("challenge", foundChallenge);

        List<Comment> comments = foundChallenge.getComments().stream().sorted(Comparator.comparing(Comment::getCommented_at).reversed()).toList();
        model.addAttribute("comments", comments);
        return "client/problem/practice";
    }
}

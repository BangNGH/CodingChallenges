package com.example.coderlab.controller.client;

import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.service.AssignmentKitService;
import com.example.coderlab.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/skills-verification")
@RequiredArgsConstructor
public class CertifyController {
    private final AssignmentKitService assignmentKitService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("assignment_kits", assignmentKitService.getAllAssignmentsKit());
        return "client/certify/index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<AssignmentKit> found_kit_by_id = assignmentKitService.findById(id);
        if (found_kit_by_id.isPresent()) {
            model.addAttribute("assignment_kit", found_kit_by_id.get());

            return "client/certify/details";
        }
        redirectAttributes.addFlashAttribute("message", "Not found certify with ID: " + id);
        return "redirect:/skills-verification";

    }

}

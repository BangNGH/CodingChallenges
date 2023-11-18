package com.example.coderlab.controller.client;

import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.entity.AssignmentKitSubmission;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.service.AssignmentKitService;
import com.example.coderlab.service.AssignmentKitSubmissionService;
import com.example.coderlab.service.TestCaseService;
import com.example.coderlab.service.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/skills-verification")
@RequiredArgsConstructor
public class CertifyController {
    private final AssignmentKitService assignmentKitService;
    private final AssignmentKitSubmissionService assignmentKitSubmissionService;
    private final UserServices userServices;
    @GetMapping
    public String index(Model model) {
        model.addAttribute("assignment_kits", assignmentKitService.getAllAssignmentsKit());
        return "client/certify/index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        Optional<AssignmentKit> found_kit_by_id = assignmentKitService.findById(id);
        if (found_kit_by_id.isPresent()) {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            List<AssignmentKitSubmission> found_assignments_kit = assignmentKitSubmissionService.getByAssignmentKit_User_Id(found_kit_by_id.get(), current_user);
            Boolean is_passed = found_assignments_kit.get(found_assignments_kit.size()-1).getIs_success();
            if (found_assignments_kit != null){
                model.addAttribute("already_tested", true);

                if (is_passed == false){
                    model.addAttribute("is_passed", false);
                }else {
                    model.addAttribute("is_passed", true);
                }
            } else {
                model.addAttribute("already_tested", false);
            }

            model.addAttribute("assignment_kit", found_kit_by_id.get());
            return "client/certify/details";
        }
        redirectAttributes.addFlashAttribute("message", "Not found certify with ID: " + id);
        return "redirect:/skills-verification";

    }
    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        Optional<AssignmentKit> found_kit_by_id = assignmentKitService.findById(id);
        if (found_kit_by_id.isPresent()) {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            model.addAttribute("assignment_kit", found_kit_by_id.get());
            model.addAttribute("user_id", current_user.getId());

            return "client/certify/certify";
        }
        redirectAttributes.addFlashAttribute("message", "Not found certify with ID: " + id);
        return "redirect:/skills-verification";

    }

}

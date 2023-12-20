package com.example.coderlab.controller.admin;

import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.entity.Contest;
import com.example.coderlab.entity.Role;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/contest")
public class ContestController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AssignmentKitService assignmentKitService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private UserServices userServices;
    @GetMapping()
    public String index(Model model){
        if(model.containsAttribute("message")){
            model.addAttribute("message", model.getAttribute("message"));
        }
        model.addAttribute("contests", contestService.getContests());
        return "admin/contest/index";
    }
    @GetMapping("/add")
    public String addKit(Model model){
        Role teacherRole = roleService.getRoleByName("TEACHER");
        model.addAttribute("contest", new Contest());
        model.addAttribute("teachers", userServices.getListRole(teacherRole));

        return "admin/contest/add";
    }
    @PostMapping("/add")
    public String add(@RequestParam("name") String name, @RequestParam("multi_coding_select[]")List<String> teachers_id, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam("formattedEndTime") Date endTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") @RequestParam("formattedStartTime") Date startTime, Principal principal, RedirectAttributes redirectAttributes){
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        Contest contest = new Contest();
        contest.setContestName(name);
        contest.setEndTime(endTime);
        contest.setStartTime(startTime);
        contest.setCreatedBy(current_user);
        contest.setIsRandomAssignment(true);

        for (String teacher_id : teachers_id
             ) {
            Optional<UserEntity> teacher = userServices.findById(Long.valueOf(teacher_id));
            if (teacher!=null) {
                contest.getTeachers().add(teacher.get());
            }
        }
        contestService.saveContest(contest);

        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/admin/contest";
    }

}

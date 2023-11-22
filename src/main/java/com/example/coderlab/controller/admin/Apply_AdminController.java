package com.example.coderlab.controller.admin;

import com.example.coderlab.entity.Apply;
import com.example.coderlab.entity.Assignment;
import com.example.coderlab.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/apply")
public class Apply_AdminController {
    @Autowired
    private ApplyService applyService;
    @GetMapping()
    public String index(Model model){
        if(model.containsAttribute("message")){
            model.addAttribute("message", model.getAttribute("message"));
        }
        model.addAttribute("applies", applyService.getAllApplies());
        return "admin/apply/index";
    }
    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("apply", new Apply());
        return "admin/apply/add";
    }
    @PostMapping("/add")
    public String addAssignment(@RequestParam("jobName") String jobName, @RequestParam("description") String description,
                                @RequestParam("companyName") String companyName, @RequestParam("address") String address,
                                @RequestParam("imageURL") MultipartFile multipartFile, RedirectAttributes redirectAttributes
                               ) throws IOException {


        applyService.addApply(jobName, companyName, address, description, multipartFile);
        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/admin/apply";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Apply apply = applyService.getApplyById(id);
        model.addAttribute("apply", apply);
        return "admin/apply/edit";
    }
    @PostMapping("/edit")
    public String edit(@ModelAttribute("assignment") Apply updateApply,
                       @RequestParam("description") String description,
                       @RequestParam("imageURL") MultipartFile multipartFile,
                       RedirectAttributes redirectAttributes) throws IOException {

        applyService.updateApply(updateApply, description, multipartFile);
        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/admin/apply";
    }
}

package com.example.coderlab.controller.client;

import com.example.coderlab.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apply")
public class ApplyController {
    @Autowired
    private ApplyService applyService;
    @GetMapping()
    public String listApply(Model model){
        model.addAttribute("applies", applyService.getAllApplies());
        return "client/apply/index";
    }
}

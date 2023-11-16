package com.example.coderlab.controller.client;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.service.AssignmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/prepare")
public class PrepareController {

    @GetMapping("")
    public String prepare(){
        return "client/prepare/index";
    }
}

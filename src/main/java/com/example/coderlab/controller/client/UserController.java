package com.example.coderlab.controller.client;

import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;
    public UserEntity getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userServices.findByEmail(email).orElseThrow();
    }
    @GetMapping("/settings")
    public String userSettings(Model model){
        UserEntity user = getUser();
        model.addAttribute("user", user);
        if(model.containsAttribute("message")){
            model.addAttribute("message", model.getAttribute("message"));
        }
        return "client/user/settings";
    }
    @PostMapping ("/settings")
    public String userSettingsPost(@ModelAttribute("user") UserEntity user, @RequestParam("image") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
        userServices.updateUser(user, multipartFile);
        redirectAttributes.addFlashAttribute("message", "Save successfully!");
        return "redirect:/user/settings";
    }
    @GetMapping("/changePassword")
    public String userChangePassword(Model model){

        return "client/user/change-password";
    }
    @PostMapping("/changePassword")
    public String userChangePasswordPost(Model model, @RequestParam("password") String newPassword,
                                         @RequestParam("currentPassword") String currentPassword){
        UserEntity user = getUser();
        if(userServices.checkPassword(user, currentPassword)){
            userServices.updatePassword(user, newPassword);
            model.addAttribute("message", "Save successfully!");
        }else {
            model.addAttribute("messageErr", "Incorrect password!!");
        }
        return "client/user/change-password";
    }
}

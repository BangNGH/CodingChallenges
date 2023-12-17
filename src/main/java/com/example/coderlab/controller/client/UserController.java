package com.example.coderlab.controller.client;

import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.entity.AssignmentKitSubmission;
import com.example.coderlab.entity.Language;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.service.*;
import com.example.coderlab.dto.LanguagePercentageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentKitSubmissionService assignmentKitSubmissionService;
    public UserEntity getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userServices.findByEmail(email).orElseThrow();
    }
    @GetMapping("/my-profile")
    public String profile(Model model){
        UserEntity user = getUser();
        Integer myPoints = userServices.getSovledAssignment(user.getId());
        Integer maxPoints = assignmentService.getAllAssignments().size();
        if (myPoints!=null){
            double percentage = ((double)myPoints / (double)maxPoints) * 100;
            percentage = Math.round(percentage * 100) / 100.0;
            model.addAttribute("percentage", percentage);
            model.addAttribute("myTotalScore", myPoints);
            model.addAttribute("maxScore", maxPoints);
        }else{
            model.addAttribute("percentage", 0);
            model.addAttribute("myTotalScore", 0);
        }
        List<Object[]> results = userServices.getLanguagePercentageByStudentId(user.getId());
        List<AssignmentKit> certifyPassedList = assignmentKitSubmissionService.getCertifyPassed(user.getId());
      if (!results.isEmpty()){
          List<LanguagePercentageDTO> language_percentages = new ArrayList<LanguagePercentageDTO>();
          for (Object[] result : results) {
              LanguagePercentageDTO language_percentage = new LanguagePercentageDTO();
              Long languageId = (Long) result[0];
              Language foundLanguage = languageService.findByLanguageID(languageId).get();
              language_percentage.setLanguage(foundLanguage);

              BigDecimal percentage = (BigDecimal) result[1];
              // Làm tròn percentage đến 2 chữ số thập phân bằng phương thức setScale
              percentage = percentage.setScale(2, RoundingMode.HALF_UP);
              // In ra giá trị hoặc làm gì đó với chúng
              language_percentage.setPercent(String.valueOf(percentage)+"%");
              language_percentages.add(language_percentage);
          }
          model.addAttribute("language_percentages", language_percentages);
      }


        model.addAttribute("user", user);
        if (!certifyPassedList.isEmpty()) {
            model.addAttribute("certifyPassedList", certifyPassedList);
        }
        model.addAttribute("user", user);
        return "client/user/profile";
    }    @GetMapping("/settings")
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
        return "redirect:/user/my-profile";
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

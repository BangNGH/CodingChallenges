package com.example.coderlab.controller.admin;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.Role;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.entity.UserRole;
import com.example.coderlab.service.AssignmentService;
import com.example.coderlab.service.RoleService;
import com.example.coderlab.service.UserRoleService;
import com.example.coderlab.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/user-management")
public class ManagementUserController {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserServices userServices;

    @GetMapping("")
    public String userManagement(Model model){
        model.addAttribute("users", userServices.getList());
        return "admin/user/index";
    }
    @GetMapping("/edit/{id}")
    public String editUserRole(@PathVariable("id") Long id, Model model){
        Optional<UserEntity> founduser = userServices.findById(id);
        List<Role> roles = roleService.getList();
        if (founduser!=null) {
            UserEntity user = founduser.get();
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            return "admin/user/edit";
        }
        return "admin/user/index";
    }
    @PostMapping("/edit")
    public String edit(@RequestParam(value = "role[]", required = false) List<String> setRoles,@RequestParam(value = "user_id") Long user_id,RedirectAttributes redirectAttributes) {
        if (setRoles!=null){
            Optional<UserEntity> founduser = userServices.findById(user_id);
            if (founduser!=null) {
                UserEntity user = founduser.get();
                for (String role : setRoles
                     ) {
                    Role foundRole = roleService.findById(role);
                    if (foundRole!=null) {
                        UserRole newUserRole = new UserRole();
                        UserRole existsRole = roleService.checkRoleIfExists(user,foundRole);
                        if (existsRole == null){

                            newUserRole.setUser(user);
                            newUserRole.setRole(foundRole);
                            userRoleService.save(newUserRole);
                            redirectAttributes.addFlashAttribute("message", "Cấp quyền "+ foundRole.getName() + " thành công!");

                        }else{
                            redirectAttributes.addFlashAttribute("message", "Người dùng này đã có quyền "+ foundRole.getName());
                        }

                    }
                }
            }
        }
        return "redirect:/admin/user-management";
    }
}

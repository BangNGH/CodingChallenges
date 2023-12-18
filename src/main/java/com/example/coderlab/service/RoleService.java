package com.example.coderlab.service;

import com.example.coderlab.entity.Role;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.entity.UserRole;
import com.example.coderlab.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getList() {
        return roleRepository.findAll();
    }

    public Role getRoleByName(String name){
        if (roleRepository.findByName(name).isPresent()){
            return roleRepository.findByName(name).get();
        }
           throw new RuntimeException("Could not find role");
    }

    public void addRole() {
        if (this.getList().isEmpty()) {
            Role adminRole = new Role();
            Role companyRole = new Role();
            Role devRole = new Role();
            adminRole.setName("ADMIN");
            devRole.setName("DEVELOPER");
            companyRole.setName("TEACHER");
            roleRepository.save(adminRole);
            roleRepository.save(devRole);
            roleRepository.save(companyRole);
            System.out.println("Added 3 roles");
        }
    }

    public Role findById(String role) {
        Optional<Role> foundRole =roleRepository.findById(Long.valueOf(role));
        if (foundRole.isPresent()) {
            return foundRole.get();
        } return null;
    }

    public UserRole checkRoleIfExists(UserEntity user, Role foundRole) {
        return roleRepository.checkRoleIfExists(user, foundRole);
    }
}

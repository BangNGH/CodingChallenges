package com.example.coderlab.service;

import com.example.coderlab.entity.Role;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.repository.RoleRepository;
import com.example.coderlab.repository.UserRepository;
import com.example.coderlab.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getList() {
        return roleRepository.findAll();
    }

    public Role getRoleByName(String name){
        System.out.println(" name: " + name);

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
            companyRole.setName("COMPANY");
            roleRepository.save(adminRole);
            roleRepository.save(devRole);
            roleRepository.save(companyRole);
        }
    }
}

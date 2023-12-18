package com.example.coderlab.service;

import com.example.coderlab.entity.UserRole;
import com.example.coderlab.repository.RoleRepository;
import com.example.coderlab.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public void save(UserRole newUserRole) {
        userRoleRepository.save(newUserRole);
    }
}

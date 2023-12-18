package com.example.coderlab.repository;

import com.example.coderlab.entity.Role;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @Query("SELECT COUNT(*) FROM Role")
    Long countById(Long id);
    @Query("SELECT r FROM UserRole r WHERE r.user =?1 and r.role =?2")
    UserRole checkRoleIfExists(UserEntity user, Role foundRole);
}
package com.example.coderlab.repository;

import com.example.coderlab.entity.Role;
import com.example.coderlab.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @Query("SELECT COUNT(*) FROM Role")
    Long countById(Long id);
}
package com.example.coderlab.repository;
import com.example.coderlab.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT COUNT(*) FROM UserRole")
    Long countById(Long id);
}

package com.example.coderlab.repository;

import com.example.coderlab.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT e FROM UserEntity e where e.email =?1 ")
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT COUNT(*) FROM UserEntity ")
    Long countById(Long id);
}


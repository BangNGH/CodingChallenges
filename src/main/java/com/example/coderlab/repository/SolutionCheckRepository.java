package com.example.coderlab.repository;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.SolutionCheck;
import com.example.coderlab.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SolutionCheckRepository extends JpaRepository<SolutionCheck, Long> {

    @Query("SELECT p FROM SolutionCheck p WHERE p.user = ?1 AND p.assignment = ?2")
    Optional<SolutionCheck> isUnlocked(UserEntity currentUser, Assignment foundChallenge);
}
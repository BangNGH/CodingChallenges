package com.example.coderlab.repository;

import com.example.coderlab.entity.Apply;
import com.example.coderlab.entity.Contest;
import com.example.coderlab.entity.ContestSubmission;
import com.example.coderlab.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestSubmissionRepository extends JpaRepository<ContestSubmission, Long> {
    @Query("SELECT p FROM ContestSubmission p WHERE p.contest = ?1 AND p.user_submitted = ?2")
    List<ContestSubmission> getContestSubmissions(Contest foundContest, UserEntity currentUser);

    @Query("SELECT p FROM ContestSubmission p WHERE p.contest = ?1 AND p.user_submitted = ?2 and p.is_success=true")
    List<ContestSubmission> getCorrectContestAnswer(Contest foundContest, UserEntity currentUser);
}

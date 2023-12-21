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

    @Query("SELECT p FROM ContestSubmission p WHERE p.contest = ?1 AND p.user_submitted = ?2")
    ContestSubmission getContestSubmission(Contest foundContest, UserEntity currentUser);

    @Query("SELECT ROW_NUMBER() OVER (ORDER BY c.correctAnswer DESC, c.totalScore DESC, c.totalTime ASC) AS stt, c.user_submitted.id as student_id, c.correctAnswer as correct_answer, c.totalScore as total_score, c.totalTime as total_time FROM ContestSubmission c where c.contest=?1\n" +
            "ORDER BY c.correctAnswer DESC, c.totalScore DESC, c.totalTime ASC")
    List<Object[]> contestRank(Contest foundContest);
}

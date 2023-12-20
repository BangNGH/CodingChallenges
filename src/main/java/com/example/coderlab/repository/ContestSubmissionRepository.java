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

    @Query("SELECT ROW_NUMBER() OVER (ORDER BY t.solved_assignments DESC) AS top, t.student_id as student_id, t.solved_assignments as solved_assignments\n" +
            "FROM (\n" +
            "  SELECT s.user_submitted.id as student_id, COUNT(DISTINCT s.contest.id) as solved_assignments\n" +
            "  FROM ContestSubmission as s\n" +
            "  WHERE s.is_success = true and s.contest=?1\n" +
            "  GROUP BY s.user_submitted.id\n" +
            ") AS t\n" +
            "ORDER BY t.solved_assignments DESC\n"+
            "LIMIT 100\n")
    List<Object[]> contestRank(Contest foundContest);
}

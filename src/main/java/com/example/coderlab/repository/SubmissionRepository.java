package com.example.coderlab.repository;

import com.example.coderlab.entity.Submission;
import com.example.coderlab.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    @Query("SELECT p FROM Submission p WHERE p.student.id = ?1 AND p.assignment.id = ?2")
    List<Submission> getSubmissions(Long userID, Long assignmentID);

    @Query("SELECT ROW_NUMBER() OVER (ORDER BY t.solved_assignments DESC) AS top, t.student_id as student_id, t.solved_assignments as solved_assignments\n" +
            "FROM (\n" +
            "  SELECT s.student.id as student_id, COUNT(DISTINCT s.assignment.id) as solved_assignments\n" +
            "  FROM Submission as s\n" +
            "  WHERE s.is_success = true\n" +
            "  GROUP BY s.student.id\n" +
            ") AS t\n" +
            "ORDER BY t.solved_assignments DESC\n"+
            "LIMIT 100\n")
    List<Object[]> rankByAssignment();
}


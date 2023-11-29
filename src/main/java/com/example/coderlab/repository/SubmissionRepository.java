package com.example.coderlab.repository;

import com.example.coderlab.entity.Submission;
import com.example.coderlab.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    @Query("SELECT p FROM Submission p WHERE p.student.id = ?1 AND p.assignment.id = ?2")
    List<Submission> getSubmissions(Long userID, Long assignmentID);
}


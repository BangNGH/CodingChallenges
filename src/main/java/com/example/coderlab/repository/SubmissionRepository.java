package com.example.coderlab.repository;

import com.example.coderlab.entity.Submission;
import com.example.coderlab.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

}


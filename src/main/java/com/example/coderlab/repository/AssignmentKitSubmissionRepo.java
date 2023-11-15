package com.example.coderlab.repository;

import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.entity.AssignmentKitSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentKitSubmissionRepo extends JpaRepository<AssignmentKitSubmission,Long> {
}

package com.example.coderlab.repository;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.AssignmentKitSubmission;
import com.example.coderlab.entity.Language;
import com.example.coderlab.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssignmentKitSubmissionRepo extends JpaRepository<AssignmentKitSubmission,Long> {
    @Query("SELECT p FROM AssignmentKitSubmission p WHERE p.assignment_kit.id = ?1 AND p.user_submitted.id = ?2")
    List<AssignmentKitSubmission> getByAssignmentKit_User_Id(Long assignment_kit_id, Long user_id);

    @Query("SELECT DISTINCT s.assignment_kit.id\n" +
            "FROM AssignmentKitSubmission as s\n" +
            "WHERE s.is_success = true and s.user_submitted.id = ?1")
    List<Long> getCertifyPassed(Long user_id);
}

package com.example.coderlab.repository;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    @Query("SELECT p FROM Assignment p WHERE p.language_option = ?1")
    Page<Assignment> findAssignmentByLanguageID(Pageable pageable, Language language);

    @Query("SELECT p FROM Assignment p WHERE p.language_option is null")
    Page<Assignment> findProblemSolvingAssignments(Pageable pageable);
}

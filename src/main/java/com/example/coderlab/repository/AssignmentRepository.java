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
    @Query("SELECT p FROM Assignment p WHERE p.language_option.id = ?1")
    Page<Assignment> findAssignmentByLanguageID(Long languageId, Pageable pageable);

    @Query("SELECT p FROM Assignment p WHERE p.language_option is null")
    Page<Assignment> findProblemSolvingAssignments(Pageable pageable);

    @Query("SELECT a from Assignment a where a.title like %?1% and a.language_option is null")
    Page<Assignment> searchAssignmentByName(String keyword, Pageable pageable);
    @Query("SELECT a from Assignment a where a.title like %?1% and a.language_option.id =?2")
    Page<Assignment> searchAssignmentTopicByName(String keyword ,Long languageId, Pageable pageable);
}

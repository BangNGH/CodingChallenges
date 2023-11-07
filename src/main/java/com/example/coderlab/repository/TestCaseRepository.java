package com.example.coderlab.repository;

import com.example.coderlab.entity.TestCase;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM TestCase t WHERE t.assignment.id = ?1")
    void deleteAllTestCaseById(Long id);
}

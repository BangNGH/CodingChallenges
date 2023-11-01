package com.example.coderlab.repository;

import com.example.coderlab.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

}

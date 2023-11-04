package com.example.coderlab.repository;

import com.example.coderlab.entity.Assessment;
import com.example.coderlab.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssesmentRepository extends JpaRepository<Assessment,Long> {
}


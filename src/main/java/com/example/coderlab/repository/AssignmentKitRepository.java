package com.example.coderlab.repository;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.AssignmentKit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentKitRepository extends JpaRepository<AssignmentKit,Long> {
}
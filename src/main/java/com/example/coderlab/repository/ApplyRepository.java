package com.example.coderlab.repository;

import com.example.coderlab.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
}

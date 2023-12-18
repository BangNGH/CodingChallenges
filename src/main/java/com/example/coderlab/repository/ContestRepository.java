package com.example.coderlab.repository;

import com.example.coderlab.entity.Comment;
import com.example.coderlab.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

}
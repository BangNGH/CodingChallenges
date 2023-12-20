package com.example.coderlab.repository;

import com.example.coderlab.entity.Comment;
import com.example.coderlab.entity.Contest;
import com.example.coderlab.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

}
package com.example.coderlab.repository;

import com.example.coderlab.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TagRepository extends JpaRepository<Tag, Long> {
}

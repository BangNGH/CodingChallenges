package com.example.coderlab.repository;

import com.example.coderlab.entity.Comment;
import com.example.coderlab.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {

}
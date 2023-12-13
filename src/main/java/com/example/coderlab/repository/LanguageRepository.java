package com.example.coderlab.repository;

import com.example.coderlab.entity.Comment;
import com.example.coderlab.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    @Query("SELECT p FROM Language p WHERE p.value = ?1")
    Optional<Language> findByValue(String languageValue);
}
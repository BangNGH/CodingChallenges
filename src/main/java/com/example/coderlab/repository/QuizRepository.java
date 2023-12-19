package com.example.coderlab.repository;

import com.example.coderlab.entity.Apply;
import com.example.coderlab.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM questions \n" +
            "WHERE language_id = ?1\n" +
            "AND level_id = ?2\n" +
            "ORDER BY RAND()\n" +
            "LIMIT ?3", nativeQuery = true)
    List<Question> getRandomQuizs(Long language_id, Long level_id, Integer numberOfQuiz);
    @Query(value = "SELECT * FROM questions \n" +
            "WHERE level_id = ?1\n" +
            "AND language_id is null\n" +
            "ORDER BY RAND()\n" +
            "LIMIT ?2", nativeQuery = true)
    List<Question> getRandomProblemSolvingQuiz(Long id, Integer numberOfQuiz);
    @Query(value = "SELECT * FROM questions \n" +
            "ORDER BY RAND()\n" +
            "LIMIT ?1", nativeQuery = true)
    List<Question> getRandomQuizs(Integer number);
}

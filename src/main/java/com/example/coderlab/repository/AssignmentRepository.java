package com.example.coderlab.repository;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.Language;
import com.example.coderlab.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    @Query("SELECT p FROM Assignment p WHERE p.language_option.id = ?1")
    Page<Assignment> findAssignmentByLanguageID(Long languageId, Pageable pageable);

    @Query("SELECT p FROM Assignment p WHERE p.language_option is null")
    Page<Assignment> findProblemSolvingAssignments(Pageable pageable);

    @Query("SELECT a from Assignment a where a.title like %?1% and a.language_option is null")
    Page<Assignment> searchAssignmentByName(String keyword, Pageable pageable);

    @Query("SELECT a from Assignment a where a.title like %?1% and a.language_option.id =?2")
    Page<Assignment> searchAssignmentTopicByName(String keyword ,Long languageId, Pageable pageable);
    @Query("SELECT a FROM Assignment a WHERE a.language_option IS NULL " +
          "AND ((:easy = TRUE AND a.level.id = 1) OR (:medium = TRUE AND a.level.id = 2) OR (:hard = TRUE AND a.level.id = 3))")
    Page<Assignment> filterAssignmentDefault(@Param("easy") boolean easy, @Param("medium") boolean medium, @Param("hard") boolean hard, Pageable pageable);
    @Query("SELECT a FROM Assignment a WHERE a.language_option IS NULL " +
            "AND EXISTS (SELECT s FROM Submission s WHERE s.assignment.id = a.id AND s.student.id = :userID) " +
            "AND ((:easy = TRUE AND a.level.id = 1) OR (:medium = TRUE AND a.level.id = 2) OR (:hard = TRUE AND a.level.id = 3)" +
            "OR (:easy = FALSE AND :medium = FALSE AND :hard = FALSE AND EXISTS (SELECT s FROM Submission s WHERE s.assignment.id = a.id AND s.student.id = :userID)))")
    Page<Assignment> filterAssignmentSolved(@Param("easy") boolean easy, @Param("medium") boolean medium, @Param("hard") boolean hard, @Param("userID") long userID, Pageable pageable);

    @Query("SELECT a FROM Assignment a WHERE a.language_option IS NULL " +
            "AND NOT EXISTS (SELECT s FROM Submission s WHERE s.assignment.id = a.id AND s.student.id = :userID) " +
            "AND ((:easy = TRUE AND a.level.id = 1) OR (:medium = TRUE AND a.level.id = 2) OR (:hard = TRUE AND a.level.id = 3)" +
            "OR (:easy = FALSE AND :medium = FALSE AND :hard = FALSE AND NOT EXISTS (SELECT s FROM Submission s WHERE s.assignment.id = a.id AND s.student.id = :userID)))")
    Page<Assignment> filterAssignmentUnsolved(@Param("easy") boolean easy, @Param("medium") boolean medium, @Param("hard") boolean hard, @Param("userID") long userID, Pageable pageable);

    @Query("SELECT a FROM Assignment a WHERE (a.language_option.id = :languageId)" +
        "AND ((:easy = TRUE AND a.level.id = 1) OR (:medium = TRUE AND a.level.id = 2) OR (:hard = TRUE AND a.level.id = 3))")
    Page<Assignment> filterAssignmentTopicDefault(@Param("easy") boolean easy, @Param("medium") boolean medium, @Param("hard") boolean hard, @Param("languageId") long languageId, Pageable pageable);
    @Query("SELECT a FROM Assignment a WHERE (a.language_option.id = :languageId) " +
            "AND EXISTS (SELECT s FROM Submission s WHERE s.assignment.id = a.id AND s.student.id = :userID) " +
            "AND ((:easy = TRUE AND a.level.id = 1) OR (:medium = TRUE AND a.level.id = 2) OR (:hard = TRUE AND a.level.id = 3)" +
            "OR (:easy = FALSE AND :medium = FALSE AND :hard = FALSE AND EXISTS (SELECT s FROM Submission s WHERE s.assignment.id = a.id AND s.student.id = :userID)))")
    Page<Assignment> filterAssignmentTopicSolved(@Param("easy") boolean easy, @Param("medium") boolean medium, @Param("hard") boolean hard,@Param("languageId") long languageId, @Param("userID") long userID, Pageable pageable);

    @Query("SELECT a FROM Assignment a WHERE (a.language_option.id = :languageId) " +
            "AND NOT EXISTS (SELECT s FROM Submission s WHERE s.assignment.id = a.id AND s.student.id = :userID) " +
            "AND ((:easy = TRUE AND a.level.id = 1) OR (:medium = TRUE AND a.level.id = 2) OR (:hard = TRUE AND a.level.id = 3)" +
            "OR (:easy = FALSE AND :medium = FALSE AND :hard = FALSE AND NOT EXISTS (SELECT s FROM Submission s WHERE s.assignment.id = a.id AND s.student.id = :userID)))")
    Page<Assignment> filterAssignmentTopicUnsolved(@Param("easy") boolean easy, @Param("medium") boolean medium, @Param("hard") boolean hard, @Param("languageId") long languageId, @Param("userID") long userID, Pageable pageable);
    @Query(value = "SELECT * FROM assignments \n" +
            "WHERE language_id = ?1\n" +
            "AND mark_as_certification_question is not null\n" +
            "AND level_id = ?2\n" +
            "ORDER BY RAND()\n" +
            "LIMIT ?3", nativeQuery = true)
    List<Assignment> getRandomAssignments(Long language_id, Long level_id, Integer numberOfRandomAssignment);
    @Query(value = "SELECT * FROM assignments \n" +
            "WHERE mark_as_certification_question is not null\n" +
            "AND level_id = ?1\n" +
            "ORDER BY RAND()\n" +
            "LIMIT ?2", nativeQuery = true)
    List<Assignment> getRandomProblemSolving(Long id, Integer numberOfRandomAssignment);

    @Query("SELECT p FROM Assignment p WHERE p.markAsCertificationQuestion is null")
    List<Assignment> getPracticeAssignment();

    @Query("SELECT p FROM Assignment p WHERE p.markAsCertificationQuestion is not null")
    List<Assignment> getContestAssignment();
    @Query(value = "SELECT * FROM assignments WHERE mark_as_certification_question is not null ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<Assignment> getRandomAssignments(Integer numberOfRandomAssignment);
}

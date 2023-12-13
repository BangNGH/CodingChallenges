package com.example.coderlab.repository;

import com.example.coderlab.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT e FROM UserEntity e where e.email =?1 ")
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT COUNT(*) FROM UserEntity ")
    Long countById(Long id);

    @Query("SELECT sub2.total_score \n" +
            "from(\n" +
            "\tSELECT sub.student_id as student_id, SUM(sub.max_score) AS total_score\n" +
            "\tFROM (\n" +
            "\t  SELECT s.student.id as student_id, s.assignment.id as assignment_id, MAX(s.total_score) AS max_score\n" +
            "\t  FROM Submission s \n" +
            "\t  WHERE s.is_success = true \n" +
            "\t  GROUP BY s.student.id, s.assignment.id \n" +
            "\t) sub \n" +
            "\tGROUP BY sub.student_id\n" +
            "\tORDER BY total_score DESC\n" +
            ") AS sub2\n" +
            "where sub2.student_id =?1")
    Integer getTotalScore(Long id);
}


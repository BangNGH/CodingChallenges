package com.example.coderlab.repository;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.AssignmentKit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentKitRepository extends JpaRepository<AssignmentKit,Long> {
    @Query("SELECT a from AssignmentKit a where a.title like %?1%")
    Page<AssignmentKit> searchCertifyByName(String keyword, Pageable pageable);
    @Query("SELECT a FROM AssignmentKit a WHERE " +
            "(:java = TRUE AND a.language.id = 1) OR (:csharp = TRUE AND a.language.id = 2) OR (:python = TRUE AND a.language.id = 3) OR (:cplus = TRUE AND a.language.id = 4) OR (:option = TRUE AND a.language is null)")
    Page<AssignmentKit> filterAssignmentKitByLanguages(@Param("option") boolean option, @Param("java") boolean java, @Param("csharp") boolean csharp, @Param("python") boolean python, @Param("cplus") boolean cplus, Pageable pageable);
    @Query("SELECT a FROM AssignmentKit a WHERE " +
            "EXISTS (SELECT s FROM AssignmentKitSubmission s WHERE s.assignment_kit.id = a.id AND s.user_submitted.id = :userID) " +
            "AND ((:java = TRUE AND a.language.id = 1) OR (:csharp = TRUE AND a.language.id = 2) OR (:python = TRUE AND a.language.id = 3) OR (:cplus = TRUE AND a.language.id = 4) OR (:option = TRUE AND a.language is null)" +
            "OR (:java = FALSE AND :csharp = FALSE AND :python = FALSE AND :option = FALSE AND :cplus = FALSE AND EXISTS (SELECT s FROM AssignmentKitSubmission s WHERE s.assignment_kit.id = a.id AND s.user_submitted.id = :userID)))")
    Page<AssignmentKit> filterAssignmentKitSolved(@Param("option") boolean option, @Param("java") boolean java, @Param("csharp") boolean csharp, @Param("python") boolean python, @Param("cplus") boolean cplus, @Param("userID") Long id, Pageable pageable);
    @Query("SELECT a FROM AssignmentKit a WHERE " +
            "NOT EXISTS (SELECT s FROM AssignmentKitSubmission s WHERE s.assignment_kit.id = a.id AND s.user_submitted.id = :userID) " +
            "AND ((:java = TRUE AND a.language.id = 1) OR (:csharp = TRUE AND a.language.id = 2) OR (:python = TRUE AND a.language.id = 3) OR (:cplus = TRUE AND a.language.id = 4) OR (:option = TRUE AND a.language is null)" +
            "OR (:java = FALSE AND :csharp = FALSE AND :python = FALSE AND :option = FALSE AND :cplus = FALSE AND NOT EXISTS (SELECT s FROM AssignmentKitSubmission s WHERE s.assignment_kit.id = a.id AND s.user_submitted.id = :userID)))")
    Page<AssignmentKit> filterAssignmentKitUnsolved(@Param("option") boolean option, @Param("java") boolean java, @Param("csharp") boolean csharp, @Param("python") boolean python, @Param("cplus") boolean cplus, @Param("userID") Long id, Pageable pageable);
}
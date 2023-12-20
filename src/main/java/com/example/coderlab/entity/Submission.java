package com.example.coderlab.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private UserEntity student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    private Language language;

    private int total_score;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String source_code;
    private Boolean is_success;

    @Column(name = "submited_at")
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Assessment> assessments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    private Question quiz;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignment_kit_submission_id")
    @JsonIgnore
    private AssignmentKitSubmission assignment_kit_submission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contest_submission_id")
    @JsonIgnore
    private ContestSubmission contestSubmission;
}

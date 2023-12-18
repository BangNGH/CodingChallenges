package com.example.coderlab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "assignment_kits")
public class AssignmentKit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;
    // mins
    private int time;

    private Integer numberOfQuiz;
    @OneToMany(mappedBy = "assignmentKit", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Question> quizQuestions;

    private Integer numberOfAssignment;

    @OneToMany(mappedBy = "assignmentKit", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Assignment> assignments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userAdded_id")
    @JsonIgnore
    private UserEntity user_added;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    @JsonIgnore
    private Language language;

    @OneToMany(mappedBy = "assignment_kit", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AssignmentKitSubmission> assignmentKitSubmissions;
}

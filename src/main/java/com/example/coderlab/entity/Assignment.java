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

import java.text.DecimalFormat;
    import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    @JsonIgnore
    private Language language_option;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String solution;
    //s
    private int timeLimit;
    //KB
    private int memoryLimit;
    private int max_score;
    private Boolean markAsCertificationQuestion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecturer_id")
    private UserEntity lecturer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignmentKit_id")
    private AssignmentKit assignmentKit;


    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Submission> submissions;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TestCase> testCases= new ArrayList<>();

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments= new ArrayList<>();

    //
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SolutionCheck> solutionChecks = new ArrayList<>();


    public Double getSuccessRate() {
        if (submissions == null || submissions.isEmpty()) {
            return 0.0;
        }

        long passedCount = submissions.stream().filter(submission -> submission.getIs_success()).count();
        double successRate = (double) passedCount / submissions.size() * 100;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(successRate));
    }
}   

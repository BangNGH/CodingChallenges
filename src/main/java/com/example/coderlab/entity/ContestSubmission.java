package com.example.coderlab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contest_submission")
public class ContestSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean is_success;

    private Integer correctAnswer;
    private Integer totalScore;
    private Integer totalTime;

    @Column(name = "submitted_at")
    @CreationTimestamp
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime submitted_at;

    @OneToMany(mappedBy = "contestSubmission", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Submission> submissions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contest_id")
    @JsonIgnore
    private Contest contest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user_submitted;


}

package com.example.coderlab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questions")
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String question;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String answerA;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String answerB;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String answerC;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String answerD;

    private String answer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    @JsonIgnore
    private Language language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "createdUser_id")
    private UserEntity createdUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "level_id")
    private Level level;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Submission> submissions;
}

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(length = 2000)
    private String description;

    //s
    private int timeLimit;

    //MB
    private int memoryLimit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecturer_id")
    @JsonIgnore
    private UserEntity lecturer;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Submission> submissions;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TestCase> testCases= new ArrayList<>();

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments= new ArrayList<>();
}

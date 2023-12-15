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
@Table(name = "language")
@Entity
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String value;
    private String description;

    @OneToMany(mappedBy = "language_option", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Assignment> assignments;
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AssignmentKit> assignmentKits;
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Submission> submissions;
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questions;
}
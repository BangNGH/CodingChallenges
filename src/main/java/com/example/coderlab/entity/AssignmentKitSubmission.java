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
@Entity
@Table(name = "assignment_kit_submission")
public class AssignmentKitSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean is_success;

    @OneToMany(mappedBy = "assignment_kit_submission", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Submission> submissions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignment_kit_id")
    @JsonIgnore
    private AssignmentKit assignment_kit;
}

package com.example.coderlab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

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

    // minutes
    private int time;

    private Integer numberOfQuiz;
    private Integer numberOfAssignment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userAdded_id")
    @JsonIgnore
    private UserEntity user_added;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
//    @JsonIgnore
    private Language language;
    @JsonProperty("totalParticipants")
    public int getTotalOfParticipants(){
        return assignmentKitSubmissions.size();
    }

    @OneToMany(mappedBy = "assignment_kit", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AssignmentKitSubmission> assignmentKitSubmissions;

}

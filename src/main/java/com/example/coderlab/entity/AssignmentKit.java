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
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
    @NotBlank(message = "Description is required")
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userAdded_id")
    @JsonIgnore
    private UserEntity user_added;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "assignment_kit_assignments",
            joinColumns = @JoinColumn(name = "assignment_kit_id"),
            inverseJoinColumns = @JoinColumn(name = "assignment_id"))
    private Set<Assignment> assignments;

    @OneToMany(mappedBy = "assignment_kit", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AssignmentKitSubmission> assignmentKitSubmissions;
}

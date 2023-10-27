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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;
//    private String image;
//    public String getImagesPath(){
//        if(image == null || id == null) return null;
//        return "/assignment-images/" + id + "/" + image;
//    }

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private UserEntity lecturer;

    @OneToMany(mappedBy = "assignment")
    @JsonIgnore
    private List<Submission> submissions;

    @OneToMany(mappedBy = "assignment")
    @JsonIgnore
    private List<TestCase> testCases;
}

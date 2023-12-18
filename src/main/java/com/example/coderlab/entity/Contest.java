package com.example.coderlab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "contest")
@AllArgsConstructor
@NoArgsConstructor
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contestName;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "end_date")
    private Date endTime;

    @Column(name = "latest_update")
    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date latestUpdate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "teachers_contest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contest_id")
    )
    private Set<UserEntity> teachers = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_user_id")
    private UserEntity createdBy;
}

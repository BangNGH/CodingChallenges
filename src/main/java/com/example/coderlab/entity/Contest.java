package com.example.coderlab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private Boolean isRandomAssignment;
    private Integer numberOfAssignment;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "contest_assignments",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "assignment_id")
    )
    private List<Assignment> assignments;

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

    private String latestUserUpdate;
    private Boolean active = true;

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

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ContestSubmission> contestSubmissions;

    public Boolean activeStatus() {
        LocalDateTime end = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(end)) {
            active = false;
        }
        return true;
    }

    public String timeRemain() {
        LocalDateTime end = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(end)) {
            System.out.println("Cuộc thi đã kết thúc");
            active = false;
            return "Cuộc thi đã kết thúc";
        } else {
            LocalDateTime start = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
            Duration duration = Duration.between(start, end);
            return (convertSecondsToDaysOrHours(duration.toSeconds()));
        }
    }

    public Integer maxScore() {
        Integer maxScore = 0;
        for (Assignment assignment : assignments
        ) {
            maxScore = maxScore + assignment.getMax_score();
            System.out.println(assignment.getMax_score()+" ID:"+assignment.getId());
        }
        return maxScore;
    }

    public String convertSecondsToDaysOrHours(long seconds) {
        Duration duration = Duration.ofSeconds(seconds);
        long seconds_per_day = 86400L;
        if (seconds >= seconds_per_day) {
            long days = duration.toDays();
            return days + " ngày";
        } else {
            long hours = duration.toHours();
            return hours + " giờ";
        }
    }


}

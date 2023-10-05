package com.example.coderlab.entity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "assessments")
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;

    private double score; // Điểm số cho bài tập

    private double executionTime; // Thời gian thực hiện

    private double memoryUsed; // Bộ nhớ sử dụng

    private int codeSize; // Kích thước mã nguồn

    private Boolean passed;

    // Các trường và phương thức getter/setter khác
}

package com.example.coderlab.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "test_cases")
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    @JsonIgnore
    private Assignment assignment;

    private String name; // Tên testcase
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String input; // Dữ liệu vào của test case
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String expectedOutput; // Kết quả mong đợi từ test case

    private int score; // Điểm số của test case

    private boolean markSampleTestCase; // đánh dấu là test case mẫu
    // Các trường và phương thức getter/setter khác

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Assessment> assessments;
}



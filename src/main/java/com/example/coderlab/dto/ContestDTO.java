package com.example.coderlab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ContestDTO {
     // Khai báo các trường tương ứng với các thuộc tính
     private Long contest_id;

     private Integer no_assignment;
     private Integer no_quiz;
     private Boolean is_random;
     private List<Long> selected_assignments_id;

     private List<Long> selected_questions_id;

     @Override
     public String toString() {
          return "ContestDTO{" +
                  "contest_id=" + contest_id +
                  ", no_assignment=" + no_assignment +
                  ", no_quiz=" + no_quiz +
                  ", is_random=" + is_random +
                  ", selected_assignments_id=" + selected_assignments_id +
                  ", selected_questions_id=" + selected_questions_id +
                  '}';
     }
}

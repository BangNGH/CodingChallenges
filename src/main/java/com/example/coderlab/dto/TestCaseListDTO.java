package com.example.coderlab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestCaseListDTO {

     private double executionTime;
     private long memory;
     private String my_output;
     private String expected_output;
     private String stdin;
     private Boolean ispassed;

     @Override
     public String toString() {
          return "SubmissionRequestDTO{" +
                  ", executionTime=" + executionTime +
                  ", memory=" + memory +
                  ", my_output='" + my_output + '\'' +
                  '}';
     }
}

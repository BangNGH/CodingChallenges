package com.example.coderlab.dto;

import com.example.coderlab.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContestLeaderBoardDTO {
    Long rank;
    UserEntity user;
    Integer correct_answer;
    Integer total_score;
    Integer total_time;
}

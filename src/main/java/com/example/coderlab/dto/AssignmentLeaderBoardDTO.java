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
public class AssignmentLeaderBoardDTO {
    Long rank;
    UserEntity user;
    Long solvedAssignments;

    @Override
    public String toString() {
        return "AssignmentLeaderBoardDTO{" +
                "rank=" + rank +
                ", user=" + user +
                ", solvedAssignments=" + solvedAssignments +
                '}';
    }
}

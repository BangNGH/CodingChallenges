package com.example.coderlab.service;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.SolutionCheck;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.repository.SolutionCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SolutionCheckService {
    private final SolutionCheckRepository solutionCheckRepository;

    public void save(Assignment assignment, UserEntity currentUser) {
        Optional<SolutionCheck> check_if_exists = solutionCheckRepository.isUnlocked(currentUser, assignment);
        if (check_if_exists.isPresent()) {
            System.out.println("Challenge has already been unlocked.");
        }else {
            SolutionCheck newSolutionCheck = new SolutionCheck();
            newSolutionCheck.setAssignment(assignment);
            newSolutionCheck.setUser(currentUser);
            newSolutionCheck.setUnlockSolution(true);
            solutionCheckRepository.save(newSolutionCheck);
        }

    }

    public Boolean isUnlocked(UserEntity currentUser, Assignment foundChallenge) {
        Optional<SolutionCheck> if_challenge_unlocked = solutionCheckRepository.isUnlocked(currentUser, foundChallenge);
        if (if_challenge_unlocked.isPresent()) {
            return true;
        }
        return false;
    }
}

package com.example.coderlab.service;

import com.example.coderlab.entity.Contest;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;

    public Contest saveContest(Contest contest) {
        contestRepository.save(contest);
        return contest;
    }

    public List<Contest> getContests() {
        return contestRepository.findAll();
    }

    public Optional<Contest> findById(Long contestID) {
        return contestRepository.findById(contestID);
    }

}


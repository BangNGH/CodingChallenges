package com.example.coderlab.service;

import com.example.coderlab.entity.Assessment;
import com.example.coderlab.repository.AssesmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssesmentService {
    private final AssesmentRepository assesmentRepository;


    public void save(Assessment assessment) {
        this.assesmentRepository.save(assessment);
    }
}

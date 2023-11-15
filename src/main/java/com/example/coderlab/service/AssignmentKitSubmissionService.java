package com.example.coderlab.service;

import com.example.coderlab.repository.AssignmentKitRepository;
import com.example.coderlab.repository.AssignmentKitSubmissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentKitSubmissionService {
    private final AssignmentKitSubmissionRepo assignmentKitSubmissionRepo;

}

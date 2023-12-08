package com.example.coderlab.service;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.repository.AssignmentKitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AssignmentKitService {
    private final AssignmentKitRepository assignmentKitRepository;
    private final UserServices userServices;
    private final AssignmentService assignmentService;

    public List<AssignmentKit> getAllAssignmentsKit(){
        return assignmentKitRepository.findAll();
    }
    public Optional<AssignmentKit> findById(Long id){
        return assignmentKitRepository.findById(id);
    }

    public void addKit(String title, Integer time, String description, List<Long> assignmentsKit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserEntity user = userServices.findByEmail(email).orElseThrow();

        AssignmentKit assignmentKit = new AssignmentKit();
        assignmentKit.setTitle(title);
        assignmentKit.setTime(time);
        assignmentKit.setDescription(description);
        assignmentKit.setUser_added(user);

        Set<Assignment> assignments = new HashSet<>();
        for (Long assignment_id : assignmentsKit
             ) {
            Assignment found_assignment = assignmentService.getAssignmentById(assignment_id);
            assignments.add(found_assignment);
        }
        assignmentKit.setAssignments(assignments);
       assignmentKitRepository.save(assignmentKit);
    }
}

package com.example.coderlab.service;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.TestCase;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.repository.AssignmentRepository;
import com.example.coderlab.utils.FileUploadUtil;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private TestCaseService testCaseService;
    @Autowired
    private UserServices userServices;
    public List<Assignment> getAllAssignments(){
        return assignmentRepository.findAll();
    }
    public Assignment getAssignmentById(Long id){
        return assignmentRepository.findById(id).orElseThrow();
    }
    public void addAssignment(Assignment assignment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserEntity user = userServices.findByEmail(email).orElseThrow();
        assignment.setLecturer(user);
        Assignment savedAssignment = assignmentRepository.save(assignment);
        for (TestCase testCase :savedAssignment.getTestCases()
             ) {
            testCase.setAssignment(assignment);
            testCaseService.saveTestCase(testCase);
        }
    }
    public void updateAssignment(Assignment assignment){
        Assignment existingAssignment = assignmentRepository.findById(assignment.getId()).orElse(null);
        existingAssignment.setTitle(assignment.getTitle());
        existingAssignment.setDescription(assignment.getDescription());
        assignmentRepository.save(existingAssignment);
    }
    public void deleteAssignmentById(Long id){
        assignmentRepository.deleteById(id);
    }
}

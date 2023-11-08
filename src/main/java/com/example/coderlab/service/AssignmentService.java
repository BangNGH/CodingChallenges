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
import java.util.ArrayList;
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
    public void addAssignment(String title, String description, int timeLimit, int memoryLimit,List<String> testCaseNames, List<Integer> testCaseScores, List<String> testCaseInputs, List<String> testCaseOutPuts,List<Boolean> maskSamples) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserEntity user = userServices.findByEmail(email).orElseThrow();

        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setTimeLimit(timeLimit);
        assignment.setMemoryLimit(memoryLimit);
        assignment.setLecturer(user);
        Assignment savedAssignment = assignmentRepository.save(assignment);

        for (int i = 0; i < testCaseNames.size(); i++) {
            TestCase testCase = new TestCase();
            testCase.setName(testCaseNames.get(i));
            testCase.setScore(testCaseScores.get(i));
            testCase.setInput(testCaseInputs.get(i));
            testCase.setExpectedOutput(testCaseOutPuts.get(i));
            if (maskSamples != null && i < maskSamples.size()) {
                testCase.setMarkSampleTestCase(maskSamples.get(i));
            } else {
                testCase.setMarkSampleTestCase(false);
            }
            testCase.setAssignment(savedAssignment);
            testCaseService.saveTestCase(testCase);
        }
    }
    public void updateAssignment(Assignment assignment, String description,List<String> testCaseNames, List<Integer> testCaseScores, List<String> testCaseInputs, List<String> testCaseOutPuts,List<Boolean> maskSamples){
        Assignment existingAssignment = assignmentRepository.findById(assignment.getId()).orElse(null);
        existingAssignment.setTitle(assignment.getTitle());
        existingAssignment.setDescription(description);
        existingAssignment.setMemoryLimit(assignment.getMemoryLimit());
        existingAssignment.setTimeLimit(assignment.getTimeLimit());
        assignmentRepository.save(existingAssignment);

        testCaseService.deleteAllTestCase(existingAssignment.getId());
        for (int i = 0; i < testCaseNames.size(); i++) {
            TestCase testCase = new TestCase();
            testCase.setName(testCaseNames.get(i));
            testCase.setScore(testCaseScores.get(i));
            testCase.setInput(testCaseInputs.get(i));
            testCase.setExpectedOutput(testCaseOutPuts.get(i));
            if (maskSamples != null && i < maskSamples.size()) {
                testCase.setMarkSampleTestCase(maskSamples.get(i));
            } else {
                testCase.setMarkSampleTestCase(false);
            }
            testCase.setAssignment(existingAssignment);
            testCaseService.saveTestCase(testCase);
        }
    }
    public void deleteAssignmentById(Long id){
        assignmentRepository.deleteById(id);
    }
}

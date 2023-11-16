package com.example.coderlab.service;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.TestCase;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.repository.AssignmentRepository;
import com.example.coderlab.utils.FileUploadUtil;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private LevelService levelService;
    @Autowired
    private UserServices userServices;
    public List<Assignment> getAllAssignments(){
        return assignmentRepository.findAll();
    }
    public void save(Assignment assignment){
        assignmentRepository.save(assignment);
    }
    public Assignment getAssignmentById(Long id){
        return assignmentRepository.findById(id).orElseThrow();
    }
    public void addAssignment(String title, String description, Integer timeLimit, Integer memoryLimit,List<String> testCaseNames, List<Integer> testCaseScores, List<String> testCaseInputs, List<String> testCaseOutPuts,List<Boolean> maskSamples, Long level) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserEntity user = userServices.findByEmail(email).orElseThrow();

        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setLevel(levelService.getLevelById(level));
        if (timeLimit!=null) {
            assignment.setTimeLimit(timeLimit);
        }else assignment.setTimeLimit(0);
        if (memoryLimit!=null) {
            assignment.setMemoryLimit(memoryLimit);
        }else assignment.setMemoryLimit(0);
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
        existingAssignment.setLevel(assignment.getLevel());
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
    public Page<Assignment> findPaginated(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.assignmentRepository.findAll(pageable);
    }
}

package com.example.coderlab.service;

import com.example.coderlab.entity.TestCase;
import com.example.coderlab.repository.RoleRepository;
import com.example.coderlab.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;


    public void saveTestCase(TestCase testCase) {
        testCaseRepository.save(testCase);
    }
    public void deleteAllTestCase(Long id){
        testCaseRepository.deleteAllTestCaseById(id);
    }
    public List<TestCase> getTestCases() {
        return testCaseRepository.findAll();
    }
}

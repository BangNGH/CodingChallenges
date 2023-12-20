package com.example.coderlab.service;

import com.example.coderlab.entity.*;
import com.example.coderlab.repository.AssignmentRepository;
import com.example.coderlab.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private TestCaseService testCaseService;
    @Autowired
    private LevelService levelService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private UserServices userServices;
    @Autowired
    private TagRepository tagRepository;
    public List<Assignment> getPracticeAssignment(){
        return assignmentRepository.getPracticeAssignment();
    }
    public List<Assignment> getContestAssignment(){
        return assignmentRepository.getContestAssignment();
    }
    public void save(Assignment assignment){
        assignmentRepository.save(assignment);
    }
    public void saveTags(Long assignmentID, List<Long> tagIds) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(assignmentID);
        if (optionalAssignment.isPresent()) {
            Assignment assignment = optionalAssignment.get();
            List<Tag> tags = tagRepository.findAllById(tagIds);
            assignment.setTags(new HashSet<>(tags));
            assignmentRepository.save(assignment);
        }
    }
    public Assignment getAssignmentById(Long id){
        return assignmentRepository.findById(id).orElseThrow();
    }
    public void addAssignment(String title, String description, Integer timeLimit, Integer memoryLimit, List<String> testCaseNames, List<Integer> testCaseScores, List<String> testCaseInputs, List<String> testCaseOutPuts, List<Boolean> maskSamples, Long level, String languageID, String solution, Boolean isCertificateQuestion, List<Long> tags) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Language foundLanguage = null;
        UserEntity user = userServices.findByEmail(email).orElseThrow();
        int max_score=0;
        Assignment assignment = new Assignment();
        if (isCertificateQuestion!=null) {
            assignment.setMarkAsCertificationQuestion(true);
        }else assignment.setMarkAsCertificationQuestion(null);
        assignment.setTitle(title);
        if (languageID != null) {
            Optional<Language> findLanguageById = languageService.findByLanguageID(Long.valueOf(languageID));
            if (findLanguageById.isPresent()) {
                foundLanguage = findLanguageById.get();
                assignment.setLanguage_option(foundLanguage);
                assignment.setSolution(solution);
            }
        }
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
        if(!(tags.size() == 1 && tags.get(0) == 0)){
            saveTags(assignment.getId(), tags);
        }
        if(testCaseNames.size() == 1){
            TestCase testCase = new TestCase();
            testCase.setName(testCaseNames.get(0));
            testCase.setScore(testCaseScores.get(0));
            testCase.setAssignment(savedAssignment);
            if (maskSamples != null && !maskSamples.isEmpty()) {
                testCase.setMarkSampleTestCase(maskSamples.get(0));
            } else {
                testCase.setMarkSampleTestCase(false);
            }
            if(testCaseInputs.size() >= 2 || testCaseOutPuts.size() >= 2){
                if(testCaseInputs.size() >= 2){
                    StringBuilder temp1 = new StringBuilder();
                    for (int i = 0; i < testCaseInputs.size(); i++) {
                        String temp = testCaseInputs.get(i);
                        temp1.append(temp);
                        if (i < testCaseInputs.size() - 1) {
                            temp1.append(", ");
                        }
                    }
                    testCase.setInput(temp1.toString());
                }
                if(testCaseOutPuts.size() >= 2){
                    StringBuilder temp2 = new StringBuilder();
                    for (int i = 0; i < testCaseOutPuts.size(); i++) {
                        String temp = testCaseOutPuts.get(i);
                        temp2.append(temp);
                        if (i < testCaseOutPuts.size() - 1) {
                            temp2.append(", ");
                        }
                    }
                    testCase.setExpectedOutput(temp2.toString());
                }
            }else{
                testCase.setInput(testCaseInputs.get(0));
                testCase.setExpectedOutput(testCaseOutPuts.get(0));
            }
            testCaseService.saveTestCase(testCase);
            max_score += testCase.getScore();
        }else {
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
                max_score += testCase.getScore();
            }
        }
        savedAssignment.setMax_score(max_score);

        if (foundLanguage!=null) {
            foundLanguage.getAssignments().add(savedAssignment);
            languageService.save(foundLanguage);
        }
        assignmentRepository.save(savedAssignment);
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
        TestCase testCase = new TestCase();

        if(testCaseNames.size() == 1){
            if(testCaseInputs.size() >= 2 || testCaseOutPuts.size() >= 2){
                testCase.setName(testCaseNames.get(0));
                testCase.setScore(testCaseScores.get(0));
                if(testCaseInputs.size() >= 2){
                    StringBuilder temp1 = new StringBuilder();
                    for (int i = 0; i < testCaseInputs.size(); i++) {
                        String temp = testCaseInputs.get(i);
                        temp1.append(temp);
                        if (i < testCaseInputs.size() - 1) {
                            temp1.append(", ");
                        }
                    }
                    testCase.setInput(temp1.toString());
                }
                else
                    testCase.setInput(testCaseInputs.get(0));
                if(testCaseOutPuts.size() >= 2){
                    StringBuilder temp2 = new StringBuilder();
                    for (int i = 0; i < testCaseOutPuts.size(); i++) {
                        String temp = testCaseOutPuts.get(i);
                        temp2.append(temp);
                        if (i < testCaseOutPuts.size() - 1) {
                            temp2.append(", ");
                        }
                    }
                    testCase.setExpectedOutput(temp2.toString());
                }
                else
                    testCase.setExpectedOutput(testCaseOutPuts.get(0));
                if (maskSamples != null && !maskSamples.isEmpty()) {
                    testCase.setMarkSampleTestCase(maskSamples.get(0));
                } else {
                    testCase.setMarkSampleTestCase(false);
                }
            }
        }
        else{
            for (int i = 0; i < testCaseNames.size(); i++) {
                testCase.setName(testCaseNames.get(i));
                testCase.setScore(testCaseScores.get(i));
                testCase.setInput(testCaseInputs.get(i));
                testCase.setExpectedOutput(testCaseOutPuts.get(i));
                if (maskSamples != null && i < maskSamples.size()) {
                    testCase.setMarkSampleTestCase(maskSamples.get(i));
                } else {
                    testCase.setMarkSampleTestCase(false);
                }
            }
        }
        testCase.setAssignment(existingAssignment);
        testCaseService.saveTestCase(testCase);
    }
    public void deleteAssignmentById(Long id){
        assignmentRepository.deleteById(id);
    }
    public Page<Assignment> findPaginated(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.assignmentRepository.findProblemSolvingAssignments(pageable);
    }
//    public Page<Assignment> findPaginatedByTopic(int pageNo, int pageSize,String languageID){
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//        Language foundLanguage = null;
//        Optional<Language> findLanguageById = languageService.findByLanguageID(Long.valueOf(languageID));
//        if (findLanguageById.isPresent()) {
//            foundLanguage = findLanguageById.get();
//        }
//        if (foundLanguage != null) {
//            return this.assignmentRepository.findAssignmentByLanguageID(pageable, foundLanguage);
//        }
//        return this.assignmentRepository.findAll(pageable);
//    }
    public Page<Assignment> searchAssignmentByName(String keyword, Pageable pageable){
        return assignmentRepository.searchAssignmentByName(keyword, pageable);
    }
    public Page<Assignment> searchAssignmentTopicByName(String keyword, Long languageId, Pageable pageable){
        return assignmentRepository.searchAssignmentTopicByName(keyword, languageId, pageable);
    }
    public Page<Assignment> listAssignmentByTopic(Long languageId, Pageable pageable){
        return assignmentRepository.findAssignmentByLanguageID(languageId, pageable);
    }
    public Page<Assignment> filterAssignmentDefault(boolean easy, boolean medium, boolean hard, Pageable pageable){
        return assignmentRepository.filterAssignmentDefault(easy, medium, hard,  pageable);
    }
    public Page<Assignment> filterAssignmentSolved(boolean easy, boolean medium, boolean hard, long userID, Pageable pageable){
        return assignmentRepository.filterAssignmentSolved(easy, medium, hard, userID, pageable);
    }
    public Page<Assignment> filterAssignmentUnsolved(boolean easy, boolean medium, boolean hard, long userID, Pageable pageable){
        return assignmentRepository.filterAssignmentUnsolved(easy, medium, hard, userID, pageable);
    }
    public Page<Assignment> filterAssignmentTopicDefault(boolean easy, boolean medium, boolean hard, long languageId, Pageable pageable){
        return assignmentRepository.filterAssignmentTopicDefault(easy, medium, hard, languageId, pageable);
    }
    public Page<Assignment> filterAssignmentTopicSolved(boolean easy, boolean medium, boolean hard, long languageId, long userID, Pageable pageable){
        return assignmentRepository.filterAssignmentTopicSolved(easy, medium, hard, languageId, userID, pageable);
    }
    public Page<Assignment> filterAssignmentTopicUnsolved(boolean easy, boolean medium, boolean hard, long languageId, long userID, Pageable pageable){
        return assignmentRepository.filterAssignmentTopicUnsolved(easy, medium, hard, languageId, userID, pageable);
    }
    public Page<Assignment> findProblemSolvingAssignments(Pageable pageable) {
        return assignmentRepository.findProblemSolvingAssignments(pageable);
    }

    public Integer getMaxScore() {
        List<Assignment> assignments = assignmentRepository.findAll();
        if (!assignments.isEmpty()){
            AtomicInteger maxScore = new AtomicInteger(0);
            assignments.forEach(assignment -> maxScore.addAndGet(assignment.getMax_score()));
            return maxScore.get();
        }
        return 0;
    }

    public List<Assignment> getRandomAssignments(Integer numberOfRandomAssignment, Language language, Level level) {
        if (language == null) {
            return assignmentRepository.getRandomProblemSolving(level.getId(), numberOfRandomAssignment);
        }
        return assignmentRepository.getRandomAssignments(language.getId(), level.getId(), numberOfRandomAssignment);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> getRandomAssignments(Integer numberOfAssignment) {
        return assignmentRepository.getRandomAssignments(numberOfAssignment);
    }
}

package com.example.coderlab.service;

import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.repository.AssignmentKitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public void save(AssignmentKit assignmentKit) {
        assignmentKitRepository.save(assignmentKit);
    }
    public Page<AssignmentKit> searchCertifyByName(String keyword, Pageable pageable){
        return assignmentKitRepository.searchCertifyByName(keyword, pageable);
    }
    public Page<AssignmentKit> getAll(Pageable pageable){
        return assignmentKitRepository.findAll(pageable);
    }
    public Page<AssignmentKit> filterAssignmentKitByLanguages(boolean option, boolean java, boolean csharp,boolean python, boolean cplus, Pageable pageable){
        return assignmentKitRepository.filterAssignmentKitByLanguages(option, java, csharp, python, cplus, pageable);
    }
    public Page<AssignmentKit> filterAssignmentKitSolved(boolean option, boolean java, boolean csharp,boolean python, boolean cplus, long userID, Pageable pageable){
        return assignmentKitRepository.filterAssignmentKitSolved(option,java,csharp,python,cplus,userID,pageable);
    }
    public Page<AssignmentKit> filterAssignmentKitUnsolved(boolean option, boolean java, boolean csharp,boolean python, boolean cplus, long userID, Pageable pageable){
        return assignmentKitRepository.filterAssignmentKitUnsolved(option,java,csharp,python,cplus,userID,pageable);
    }
}

package com.example.coderlab.controller.client;


import com.example.coderlab.entity.AssignmentKit;
import com.example.coderlab.repository.AssignmentKitRepository;
import com.example.coderlab.service.AssignmentKitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/certify")
public class CertifyRestController {
    @Autowired
    private AssignmentKitService assignmentKitService;
    @Autowired
    private AssignmentKitRepository assignmentKitRepository;
    @GetMapping("")
    public Page<AssignmentKit> getAll(Pageable pageable){
        return assignmentKitRepository.findAll(pageable);
    }
    @GetMapping("/search")
    public Page<AssignmentKit> searchCertify(@RequestParam("searchText") String searchText, Pageable pageable){
        return assignmentKitService.searchCertifyByName(searchText, pageable);
    }
    @GetMapping("/filter")
    public Page<AssignmentKit> filterAssignment(@RequestParam("option") Boolean option,
                                             @RequestParam("java") Boolean java,
                                             @RequestParam("csharp") Boolean csharp,
                                             @RequestParam("python") Boolean python ,
                                             @RequestParam("cplus") Boolean cplus,
                                             @RequestParam("solved") Boolean solved,
                                             @RequestParam("unsolved") Boolean unsolved,
                                             @RequestParam("userID") Long userID, Pageable pageable){
        if(!option && !java && !cplus && !csharp && !python && !solved && !unsolved){
            return assignmentKitService.getAll(pageable);
        }
        if(solved && !unsolved){
            return assignmentKitService.filterAssignmentKitSolved(option,java,csharp,python,cplus,userID,pageable);
        }
        if(!solved && unsolved){
            return assignmentKitService.filterAssignmentKitUnsolved(option,java,csharp,python,cplus,userID,pageable);
        }
        if(solved && unsolved && !option && !java && !python && !csharp){
            return assignmentKitService.getAll(pageable);
        }else {
            return assignmentKitService.filterAssignmentKitByLanguages(option,java,csharp,python,cplus,pageable);
        }
    }
}

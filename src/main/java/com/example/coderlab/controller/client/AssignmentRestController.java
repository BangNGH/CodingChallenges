package com.example.coderlab.controller.client;

import com.example.coderlab.entity.*;
import com.example.coderlab.service.*;
import com.example.coderlab.dto.SubmissionInfoSendDTO;
import com.example.coderlab.dto.SubmissionKitInfoSendDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/submissions")
public class AssignmentRestController {
    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private AssignmentKitSubmissionService submissionKitService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserServices userServices;

    @Autowired
    private SolutionCheckService solutionCheckService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private ContestSubmissionService contestSubmissionService;

    @PostMapping("/add")
    public String handleSubmission(@RequestBody SubmissionInfoSendDTO submission, Principal principal, HttpSession session) {
        try {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            Long id_submission = submissionService.saveSubmissions(submission, current_user);
            clearSession(session);
            return id_submission.toString();
        } catch (Exception e) {
            return "1";
        }
    }

    @PostMapping("/end-test")
    public String endContest(@RequestBody SubmissionKitInfoSendDTO submissions_id, Principal principal, HttpSession session) {
        try {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            Boolean status = submissionKitService.saveSubmissions(submissions_id, current_user);
          clearSession(session);
            if (status) {
                return "passed";
            }
            return "failed";
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }
    }
    @PostMapping("/submit-contest")
    public String submitTest(@RequestBody SubmissionKitInfoSendDTO submissions_id, Principal principal, HttpSession session) {
        try {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            Boolean status = contestSubmissionService.save(submissions_id, current_user);

          clearSession(session);
            if (status) {
                return "passed";
            }
            return "failed";
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }
    }

    @GetMapping("/comment")
    @ResponseBody
    public Comment commentPost(@RequestParam("comment") String comment, @RequestParam(value = "source_code_comment", defaultValue = "false") String source_code_comment, @RequestParam("assignment_id") long assignmentID, Principal principal) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentID);
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        return commentService.save(comment, source_code_comment, assignment, current_user);
    }

    @PostMapping("/unlock-solution")
    @ResponseBody
    public void unlockSolution(@RequestParam("assignment_id") Long assignmentID, Principal principal) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentID);
        String email = principal.getName();
        UserEntity current_user = userServices.findByEmail(email).get();
        solutionCheckService.save(assignment, current_user);
    }

    @PostMapping("/time-expired")
    @ResponseBody
    public String timeExpired(@RequestParam("assignment_kit_id") Long assignment_kit_id, Principal principal, HttpSession session) {
        try {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            submissionKitService.endTestWTimeExpried(assignment_kit_id, current_user);
            clearSession(session);
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }
        return "End test.";
    }

    @GetMapping("/get-assignment-info")
    @ResponseBody
    public String getAssignmentInfo(@RequestParam("assignment_id") Long assignment_id) throws JsonProcessingException {
        Assignment assignment = assignmentService.getAssignmentById(assignment_id);
        List<TestCase> sampleTestCase = assignment.getTestCases().stream().filter(testCase -> testCase.isMarkSampleTestCase() == true).toList();
        ObjectMapper objectMapper = new ObjectMapper();
        String sampleTestCasesJson = objectMapper.writeValueAsString(sampleTestCase);
        String allTestCasesJson = objectMapper.writeValueAsString(assignment.getTestCases());
        String time_limit = objectMapper.writeValueAsString(assignment.getTimeLimit());
        String memory_limit = objectMapper.writeValueAsString(assignment.getMemoryLimit());

        return "{ \"sampleTestCases\":" + sampleTestCasesJson + ", \"allTestCases\":" + allTestCasesJson + ", \"time_limit\":" + time_limit + ", \"memory_limit\":" + memory_limit + " }";
    }

    @PostMapping("/save-content")
    public ResponseEntity<String> saveContent(@RequestParam String content, @RequestParam String mode, @RequestParam String langague_name, @RequestParam String current_tab_id, HttpSession session) throws JsonProcessingException {
        session.setAttribute("editorContent", content);
        session.setAttribute("mode", mode);
        session.setAttribute("language_name", langague_name);
        session.setAttribute("current_tab_id", current_tab_id);
        return ResponseEntity.ok("Content saved successfully!");
    }
    @PostMapping("/submit-quiz")
    public String submitQuiz(@RequestParam String answer, @RequestParam String quiz_id, Principal principal) {
        Question foundQuiz = quizService.findQuestionById(quiz_id);
        if (foundQuiz!=null) {
            String email = principal.getName();
            UserEntity current_user = userServices.findByEmail(email).get();
            Submission submission = new Submission();
            Boolean isSuccess = false;
            if (foundQuiz.getAnswer().equals(answer)) {
                isSuccess=true;
            }
            submission.setIs_success(isSuccess);
            if (foundQuiz.getLanguage()!=null){
                submission.setLanguage(foundQuiz.getLanguage());
            }
            submission.setStudent(current_user);
            submission.setQuiz(foundQuiz);
            Submission saved = submissionService.saveSubmission(submission);
            return saved.getId().toString();
        }
        return ("Quiz with id:"+quiz_id+" not found!");
    }

    @GetMapping("/get-content")
    public String getContent(HttpSession session) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(session.getAttribute("editorContent"));
        String mode = objectMapper.writeValueAsString(session.getAttribute("mode"));
        String language_name = objectMapper.writeValueAsString(session.getAttribute("language_name"));
        String current_tab_id = objectMapper.writeValueAsString(session.getAttribute("current_tab_id"));
        if (content.equals("null") || mode.equals("null")  || language_name.equals("null")  || current_tab_id.equals("null")) {
            return null;
        }
        return "{ \"content\":" + content + ", \"mode\":" + mode + ", \"language_name\":" + language_name + ", \"current_tab_id\":" + current_tab_id + " }";
    }

    @PostMapping("/clear-session")
    public ResponseEntity<String> clearSessionRequest(HttpSession session) {
        if (clearSession(session)) {
            return ResponseEntity.ok("Session cleared successfully!");
        } else return ResponseEntity.ok("Error when clear session !");

    }

    private Boolean clearSession(HttpSession session) {
        if (session != null) {
            session.removeAttribute("editorContent");
            session.removeAttribute("mode");
            session.removeAttribute("option");
            session.removeAttribute("language_name");
            session.removeAttribute("current_tab_id");
            System.out.println("Session cleared successfully!");
            return true;
        }
        return false;
    }
}

package com.example.coderlab.service;

import com.example.coderlab.entity.*;
import com.example.coderlab.repository.AssignmentKitSubmissionRepo;
import com.example.coderlab.dto.SubmissionKitInfoSendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentKitSubmissionService {
    private final AssignmentKitSubmissionRepo assignmentKitSubmissionRepo;
    private final AssignmentKitService assignmentKitService;
    private final SubmissionService submissionService;
    private final UserServices userServices;

    public Boolean saveSubmissions(SubmissionKitInfoSendDTO submissionsId, UserEntity current_user) throws IOException {
        Optional<AssignmentKit> found_assignment_kit = assignmentKitService.findById(Long.valueOf(submissionsId.getAssignment_kit_id()));
        Boolean is_passed = true;
        if (found_assignment_kit.isPresent()) {
            AssignmentKitSubmission assignmentKitSubmission = new AssignmentKitSubmission();
            assignmentKitSubmission.setAssignment_kit(found_assignment_kit.get());
            assignmentKitSubmission.setUser_submitted(current_user);
            AssignmentKitSubmission savedAssignmentKitSubmission = assignmentKitSubmissionRepo.save(assignmentKitSubmission);


            List<Submission> submissions = new ArrayList<>();
            List<String> submissions_id= submissionsId.getSubmissions_id_ofAssignment();
            List<String> submissions_id_ofQuiz = submissionsId.getSubmissions_id_ofQuiz();
            submissions_id.addAll(submissions_id_ofQuiz);
            System.out.println(submissions_id);
            for (String submission_id : submissions_id
                 ) {
                Submission found_submission = submissionService.getSubmission(Long.parseLong(submission_id)).get();
                found_submission.setAssignment_kit_submission(savedAssignmentKitSubmission);
                if (found_submission.getIs_success()==false){
                    is_passed= false;
                }
                submissionService.saveSubmission(found_submission);
                submissions.add(found_submission);
            }

            savedAssignmentKitSubmission.setIs_success(is_passed);
            savedAssignmentKitSubmission.setSubmissions(submissions);
            assignmentKitSubmissionRepo.save(savedAssignmentKitSubmission);

            current_user.getAssignmentKitSubmissions().add(savedAssignmentKitSubmission);
            userServices.save(current_user);

        }

        if (is_passed) {
            //absolute path to \client_assets\img\certificate.png"
            String templatePath = "D:\\Hutech\\DACN\\project\\src\\main\\resources\\static\\client_assets\\img\\certificate.png";
            // /certify-images/{user_id}/assignment_id/certify-images.png
            String outputPath = "./certify-images/" + current_user.getId()+ "/certify_"+found_assignment_kit.get().getId()+".png";
            generateCertificate(current_user.getFullName(), found_assignment_kit.get().getTitle(),templatePath, current_user, outputPath);
        }
        return is_passed;
    }
    private void generateCertificate(String username, String descriptionText , String templatePath, UserEntity current_user, String outputPath) throws IOException {

        // Load template image
        BufferedImage image = ImageIO.read(new File(templatePath));

        // Create graphics object
        Graphics g = image.getGraphics();

        // Set font and color
        Font usernameFont = new Font("Times New Roman", Font.BOLD, 60);
        Font descriptionFont = new Font("Times New Roman", Font.ITALIC, 40);

        g.setFont(usernameFont);
        g.setColor(java.awt.Color.BLACK);

        // Get font metrics to calculate text width
        FontMetrics usernameMetrics = g.getFontMetrics(usernameFont);
        int usernameX = (image.getWidth() - usernameMetrics.stringWidth(username)) / 2;
        int usernameY = (image.getHeight() - usernameMetrics.getHeight()) / 2 + usernameMetrics.getAscent();

        // Draw username on the image
        g.drawString(username, usernameX, usernameY);

        // Get font metrics for description text
        g.setFont(descriptionFont);

        Rectangle descriptionRect = new Rectangle(353, 1125, 400, 6); // Define your rectangle here

        // Get font metrics for description text
        g.setFont(descriptionFont);
        FontMetrics descriptionMetrics = g.getFontMetrics(descriptionFont);

        // Calculate X and Y for description text to center it within the rectangle
        int descriptionX = descriptionRect.x + (descriptionRect.width - descriptionMetrics.stringWidth(descriptionText)) / 2;
        int descriptionY = descriptionRect.y + ((descriptionRect.height - descriptionMetrics.getHeight()) / 2) + descriptionMetrics.getAscent();
        g.drawString(descriptionText, descriptionX, descriptionY);

        // Dispose of the graphics object
        g.dispose();

        String uploadDir = "./certify-images/" + current_user.getId();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        ImageIO.write(image, "png", new File(outputPath));
    }

    public List<AssignmentKitSubmission> getByAssignmentKit_User_Id(AssignmentKit assignmentKit, UserEntity currentUser) {
        List<AssignmentKitSubmission> foundAssignmentKitSubmission = assignmentKitSubmissionRepo.getByAssignmentKit_User_Id(assignmentKit.getId(), currentUser.getId());
        return foundAssignmentKitSubmission;
    }

    public void endTestWTimeExpried(Long assignmentKitId, UserEntity currentUser) {
        Optional<AssignmentKit> found_assignment_kit = assignmentKitService.findById(assignmentKitId);
        if (found_assignment_kit.isPresent()) {
            AssignmentKitSubmission assignmentKitSubmission = new AssignmentKitSubmission();
            assignmentKitSubmission.setIs_success(false);
            assignmentKitSubmission.setUser_submitted(currentUser);
            assignmentKitSubmission.setAssignment_kit(found_assignment_kit.get());
            assignmentKitSubmissionRepo.save(assignmentKitSubmission);
        }
    }

    public List<AssignmentKit> getCertifyPassed(Long id) {
        List<Long> assignmentsKitID= assignmentKitSubmissionRepo.getCertifyPassed(id);
        List<AssignmentKit> certifyPassedList=new ArrayList<AssignmentKit>();
        for (Long assignmentKitID : assignmentsKitID
        ) {
            Optional<AssignmentKit> foundAssignmentKit = assignmentKitService.findById(assignmentKitID);
            if (foundAssignmentKit.isPresent()) {
                certifyPassedList.add(foundAssignmentKit.get());
            }
        }
        return certifyPassedList;
    }
}

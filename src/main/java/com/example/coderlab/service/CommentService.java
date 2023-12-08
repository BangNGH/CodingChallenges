package com.example.coderlab.service;

import com.example.coderlab.entity.Assignment;
import com.example.coderlab.entity.Comment;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment save(String comment, String source_code_comment, Assignment assignment, UserEntity user) {
        Comment newComment = new Comment();
        newComment.setAssignment(assignment);
        newComment.setUser(user);
        if (source_code_comment != "false"){
            newComment.setSource_code(source_code_comment);
        }else newComment.setSource_code("false");
        newComment.setComment(comment);
        commentRepository.save(newComment);
        return newComment;
    }
}

package com.rakesh.instagramBackend.service;

import com.rakesh.instagramBackend.model.Comment;
import com.rakesh.instagramBackend.repository.ICommentRepo;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    @Autowired
    ICommentRepo iCommentRepo;
    public String addComment(Comment comment) {
        comment.setCommentCreationTimeStamp(LocalDateTime.now());
        iCommentRepo.save(comment);
        return "Commented";
    }

    public Comment findComment(Integer commentId) {
        Comment comment = iCommentRepo.findById(commentId).orElse(null);
        return comment;
    }

    public void removeCommment(Comment comment) {
        iCommentRepo.delete(comment);
    }
}

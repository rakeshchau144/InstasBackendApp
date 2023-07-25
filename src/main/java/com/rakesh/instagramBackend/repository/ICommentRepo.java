package com.rakesh.instagramBackend.repository;

import com.rakesh.instagramBackend.model.Admin;
import com.rakesh.instagramBackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepo extends JpaRepository<Comment, Integer> {
}

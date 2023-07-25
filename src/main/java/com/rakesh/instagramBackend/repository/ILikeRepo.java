package com.rakesh.instagramBackend.repository;

import com.rakesh.instagramBackend.model.Admin;
import com.rakesh.instagramBackend.model.Like;
import com.rakesh.instagramBackend.model.Post;
import com.rakesh.instagramBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ILikeRepo extends JpaRepository<Like, Integer> {
    List<Like> findByInstaPostAndLiker(Post instaPost, User liker);

    List<Like> findByInstaPost(Post validPost);
}

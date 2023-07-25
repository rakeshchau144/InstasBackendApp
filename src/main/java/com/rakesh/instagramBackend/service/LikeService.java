package com.rakesh.instagramBackend.service;

import com.rakesh.instagramBackend.model.Like;
import com.rakesh.instagramBackend.model.Post;
import com.rakesh.instagramBackend.model.User;
import com.rakesh.instagramBackend.repository.ILikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    ILikeRepo iLikeRepo;
    public String addLike(Like like) {
        iLikeRepo.save(like);
        return "Liked";
    }

    public boolean isLikeAllowedOnThisPost(Post instaPost, User liker) {
        List<Like> likeList = iLikeRepo.findByInstaPostAndLiker(instaPost,liker);
        return likeList !=null && likeList.isEmpty();
    }

    public Integer getLikeCountForPost(Post validPost) {
        return iLikeRepo.findByInstaPost(validPost).size();
    }

    public Like findLike(Integer likeId) {
        return iLikeRepo.findById(likeId).orElse(null);
    }

    public void removeLike(Like like) {
        iLikeRepo.delete(like);
    }
}

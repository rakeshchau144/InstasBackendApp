package com.rakesh.instagramBackend.service;

import com.rakesh.instagramBackend.model.Post;
import com.rakesh.instagramBackend.model.User;
import com.rakesh.instagramBackend.repository.IPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    @Autowired
    IPostRepo iPostRepo;
    public String createInstaPost(Post post) {
        post.setPostCreatedTimeStamp(LocalDateTime.now());
        iPostRepo.save(post);
        return "Post uploaded !";

    }

    public String removeInstaPost(Integer postId, User user) {
        Post post = iPostRepo.findById(postId).orElse(null);
        if(post !=null && post.getPostOwner().equals(user)){
            iPostRepo.deleteById(postId);
            return "Post Deleted !!";
        }
        else if(post !=null){
            return "PostId not found !";
        }
        return "Deleting not .......Allowed to you !!";

    }

    public boolean validPost(Post instaPost) {
        return (instaPost !=null && iPostRepo.existsById(instaPost.getPostId()));
    }

    public Post validPost(Integer postId) {
        return iPostRepo.findById(postId).orElse(null);
    }
}

package com.rakesh.instagramBackend.service;

import com.rakesh.instagramBackend.model.Follow;
import com.rakesh.instagramBackend.model.User;
import com.rakesh.instagramBackend.repository.IFollowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    @Autowired
    IFollowRepo iFollowRepo;
    public void addfollowing(Follow follow, User follower) {
        follow.setCurrentUserFollower(follower);
        iFollowRepo.save(follow);
    }

    public boolean isFollowAllowed(User followerTargetUser, User follower) {
        List<Follow> followList= iFollowRepo.findByCurrentUserAndCurrentUserFollower(followerTargetUser,follower);
        return followList != null && followList.isEmpty() && !followerTargetUser.equals(follower);
    }

    public Follow findFollow(Integer followingId) {
        return iFollowRepo.findById(followingId).orElse(null);
    }

    public void unFollow(Follow follow) {
        iFollowRepo.delete(follow);
    }
}

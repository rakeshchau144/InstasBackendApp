package com.rakesh.instagramBackend.repository;

import com.rakesh.instagramBackend.model.Admin;
import com.rakesh.instagramBackend.model.Follow;
import com.rakesh.instagramBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFollowRepo extends JpaRepository<Follow, Integer> {
    List<Follow> findByCurrentUserAndCurrentUserFollower(User followerTargetUser, User follower);
}

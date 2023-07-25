package com.rakesh.instagramBackend.repository;

import com.rakesh.instagramBackend.model.Admin;
import com.rakesh.instagramBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepo extends JpaRepository<User, Integer> {
    User findFirstByUserEmail(String newEmail);
}

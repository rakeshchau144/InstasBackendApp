package com.rakesh.instagramBackend.repository;

import com.rakesh.instagramBackend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdminRepo extends JpaRepository<Admin, Long> {
}

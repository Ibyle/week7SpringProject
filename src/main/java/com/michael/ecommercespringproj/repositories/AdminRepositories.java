package com.michael.ecommercespringproj.repositories;

import com.michael.ecommercespringproj.models.Admin;
import com.michael.ecommercespringproj.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepositories extends JpaRepository<Admin, Long> {
        Optional<Admin> findByUsername(String username);
        }


package com.michael.ecommercespringproj.repositories;

import com.michael.ecommercespringproj.dtos.PasswordDTO;
import com.michael.ecommercespringproj.models.Admin;

import java.util.function.Function;

public interface AdminService {
    Function<String, Admin> findAdminByUsername();

    Function<Long, Admin> findAdminById();

    Function<Admin, Admin> saveAdmin();

    Function<PasswordDTO, Boolean> verifyAdminPassword();
}

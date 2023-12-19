package com.michael.ecommercespringproj.serviceImpl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.michael.ecommercespringproj.dtos.PasswordDTO;
import com.michael.ecommercespringproj.models.Admin;
import com.michael.ecommercespringproj.repositories.AdminRepositories;
import com.michael.ecommercespringproj.repositories.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepositories adminRepositories;

    @Autowired
    public AdminServiceImpl(AdminRepositories adminRepositories) {
        this.adminRepositories = adminRepositories;
    }

    @Override
    public Function<String, Admin> findAdminByUsername() {
        return (username) -> adminRepositories.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("Admin not found"));
    }

    @Override
    public Function<Long, Admin> findAdminById() {
        return (id) -> adminRepositories.findById(id)
                .orElseThrow(() -> new NullPointerException("User not found!"));
    }

    @Override
    public Function<Admin, Admin> saveAdmin() {
        return (admin) -> adminRepositories.save(admin);
    }

    @Override
    public Function<PasswordDTO, Boolean> verifyAdminPassword() {
        return passwordDTO -> BCrypt.verifyer()
                .verify(passwordDTO.getPassword().toCharArray(),
                        passwordDTO.getHashPassword().toCharArray())
                .verified;
    }
}

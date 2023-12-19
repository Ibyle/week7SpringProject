package com.michael.ecommercespringproj.serviceImpl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.michael.ecommercespringproj.dtos.PasswordDTO;
import com.michael.ecommercespringproj.models.Users;
import com.michael.ecommercespringproj.repositories.UsersRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service  // Indicates that this class is a Spring service component,used for business logic.
public class UsersServiceImpl {

    private UsersRepositories usersRepositories;

    @Autowired  // Enables automatic dependency injection of UsersRepositories into the constructor,
    // providing access to the data repository.


    public UsersServiceImpl(UsersRepositories usersRepositories) {
        this.usersRepositories = usersRepositories;
    }

    // Function to find a user by username
    public Function<String, Users> findUsersByUsername = (username) ->
            usersRepositories.findByUsername(username)
                    .orElseThrow(() -> new NullPointerException("User not found!"));

    // Function to find a user by ID
    //        findUsersById: A function that finds a user by ID from the repository.

    public Function<Long, Users> findUsersById = (id) ->
            usersRepositories.findById(id)
                    .orElseThrow(() -> new NullPointerException("User not found!"));

    // Function to save a user to the repository
    public Function<Users, Users> saveUser = (user) -> usersRepositories.save(user);

    // Function to verify user password using BCrypt, a password hashing library
    public Function<PasswordDTO, Boolean> verifyUserPassword = passwordDTO ->
            BCrypt.verifyer()
                    .verify(passwordDTO.getPassword().toCharArray(),
                            passwordDTO.getHashPassword().toCharArray())
                    .verified;
}








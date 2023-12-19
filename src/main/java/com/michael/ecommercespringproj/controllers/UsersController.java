package com.michael.ecommercespringproj.controllers;

import com.michael.ecommercespringproj.dtos.PasswordDTO;
import com.michael.ecommercespringproj.dtos.UsersDTO;
import com.michael.ecommercespringproj.models.Users;
import com.michael.ecommercespringproj.serviceImpl.UsersServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller  //@Controller: This annotation marks the
// class as a Spring MVC controller, indicating that it will handle incoming HTTP requests.

@RequestMapping("/user")  //@RequestMapping("/user"): This annotation at the class
// level specifies the base URL path ("/user") for all the handler methods within this controller.

@Slf4j  //@Slf4j: Lombok annotation that automatically generates a logger
// field named log for logging. It eliminates the need to explicitly create a logger in the class.

public class UsersController {
    private UsersServiceImpl usersService;

    // Constructor injection of UsersServiceImpl
    @Autowired
//@Autowired: This annotation is used for automatic dependency injection.
// It injects an instance of UsersServiceImpl into the constructor of the UsersController class.

    public UsersController(UsersServiceImpl usersService) {
        this.usersService = usersService;
    }

    // Handler method for GET request to "/user/register"
    @GetMapping("/register")
    public String indexPage(Model model){
        // Add an empty UsersDTO object to the model and return the "sign-up" view
        model.addAttribute("user", new UsersDTO());
        return "sign-up";
    }

    // Handler method for GET request to "/user/login"
    //@GetMapping("/register") and @GetMapping("/login"): These annotations
// define handler methods for HTTP GET requests to "/user/register" and "/user/login", respectively.
    @GetMapping("/login")
    public ModelAndView loginPage(){
        // Return a new ModelAndView with the "login" view and an empty UsersDTO object
        return  new ModelAndView("login")
                .addObject("user", new UsersDTO());
    }

    // Handler method for POST request to "/user/sign-up"
    //@PostMapping("/sign-up") and @PostMapping("/login"): These annotations define
    // handler methods for HTTP POST requests to "/user/sign-up" and "/user/login", respectively.
//    @ModelAttribute: This annotation binds the form data in the request to the UsersDTO parameter
//    in the signUp and loginUser methods. It helps in populating the UsersDTO object with the form data.
    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute UsersDTO usersDTO){
        // Create a Users object from the UsersDTO and save it using the UsersServiceImpl
        Users user = usersService.saveUser.apply(new Users(usersDTO));
        // Log user details and return the "successful-register" view
        log.info("User details ---> {}", user);
        return "successful-register";
    }

    // Handler method for POST request to "/user/login"
    @PostMapping("/login")
    public String loginUser(@ModelAttribute UsersDTO usersDTO, HttpServletRequest request, Model model){
        // Find user by username using the UsersServiceImpl
        Users user = usersService.findUsersByUsername.apply(usersDTO.getUsername());
        // Log user details
        log.info("User details ---> {}", user);

        // Verify user password using the UsersServiceImpl
        if (usersService.verifyUserPassword
                .apply(PasswordDTO.builder()
                        .password(usersDTO.getPassword())
                        .hashPassword(user.getPassword())
                        .build())){
            // If password is verified, create a session and redirect to "/products/all"
            HttpSession session = request.getSession();
            session.setAttribute("userID", user.getId());
            return "redirect:/products/all";
        }

        // If password is not verified, redirect to "/user/login"
        return "redirect:/user/login";
    }

    // Handler method for GET request to "/user/logout"

//    @GetMapping("/logout"): This annotation defines a handler method for HTTP GET requests to "/user/logout".
//        Each of these annotations serves a specific purpose in configuring the Spring MVC controller, handling request mappings, enabling dependency injection, and simplifying logging.

    @GetMapping("/logout")
    public String logout(HttpSession session){
        // Invalidate the session and return the "index" view
        session.invalidate();
        return "index";
    }
}

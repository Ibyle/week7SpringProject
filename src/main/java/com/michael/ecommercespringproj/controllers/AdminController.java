package com.michael.ecommercespringproj.controllers;

import com.michael.ecommercespringproj.dtos.AdminDTO;
import com.michael.ecommercespringproj.dtos.PasswordDTO;
import com.michael.ecommercespringproj.models.Admin;
import com.michael.ecommercespringproj.repositories.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/register")
    public String indexPage(Model model){
        model.addAttribute("admin", new AdminDTO());
        return "admin-register";
    }

    @GetMapping("/admin-login")
    public ModelAndView loginPage(){
        return  new ModelAndView("login")
                .addObject("admin", new AdminDTO());
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute AdminDTO adminsDTO){
        Admin admin = adminService.saveAdmin().apply(new Admin(adminsDTO));
        log.info("admin details ---> {}", admin);
        return "admin-successful-register";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute AdminDTO adminsDTO, HttpServletRequest request, Model model){
        Admin admin = adminService.findAdminByUsername().apply(adminsDTO.getUsername());
        log.info("admin details ---> {}", admin);
        if (adminService.verifyAdminPassword()
                .apply(PasswordDTO.builder()
                        .password(adminsDTO.getPassword())
                        .hashPassword(admin.getPassword())
                        .build())){
            HttpSession session = request.getSession();
            session.setAttribute("adminID", admin.getId());
            return "redirect:/products/all";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }
}

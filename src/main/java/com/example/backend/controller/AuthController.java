package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        service.register(user);
        return "Registered Successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpSession session) {
        User validUser = service.login(user.getEmail(), user.getPassword());

        if (validUser != null) {
            session.setAttribute("userId", validUser.getId());
            session.setAttribute("role", validUser.getRole());
            return validUser.getRole();
        }
        return "Invalid";
    }

    // 🔐 Authorization Example
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        String role = (String) session.getAttribute("role");

        if (role == null) return "Please login";

        if (role.equals("ADMIN")) return "Welcome Admin";
        if (role.equals("RECRUITER")) return "Welcome Recruiter";
        if (role.equals("JOBSEEKER")) return "Welcome Jobseeker";

        return "Unauthorized";
    }
}
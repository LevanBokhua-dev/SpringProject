package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.Admin;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.AdminRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/user")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "register-user";
    }

    @PostMapping("/user")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register-user";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        model.addAttribute("isAdmin", false);
        return "register-success";
    }

    @GetMapping("/admin")
    public String showAdminForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "register-admin";
    }

    @PostMapping("/admin")
    public String registerAdmin(@Valid @ModelAttribute("admin") Admin admin, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register-admin";
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        model.addAttribute("isAdmin", true);
        return "register-success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "register-success";
    }
}

package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.model.Admin;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.AdminRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    // ----------- USER LOGIN -----------

    @GetMapping("/user")
    public String showUserLogin(Model model) {
        model.addAttribute("user", new UserDTO());
        return "login-user";
    }

    @PostMapping("/user")
    public String loginUser(@ModelAttribute("user") UserDTO userDTO,
                            HttpSession session,
                            Model model) {
        Optional<User> user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(userDTO.getUsername()))
                .findFirst();

        if (user.isPresent() && passwordEncoder.matches(userDTO.getPassword(), user.get().getPassword())) {
            session.setAttribute("user", user.get().getUsername());
            return "redirect:/login/welcome-user";  // ✅ Redirect to GET method
        }

        model.addAttribute("error", "Invalid credentials");
        return "login-user";
    }

    // ----------- ADMIN LOGIN -----------

    @GetMapping("/admin")
    public String showAdminLogin(Model model) {
        model.addAttribute("admin", new Admin());
        return "login-admin";
    }

    @PostMapping("/admin")
    public String loginAdmin(@ModelAttribute("admin") Admin admin,
                             HttpSession session,
                             Model model) {
        Optional<Admin> existing = adminRepository.findAll().stream()
                .filter(a -> a.getUsername().equals(admin.getUsername()) &&
                        passwordEncoder.matches(admin.getPassword(), a.getPassword()))
                .findFirst();

        if (existing.isPresent()) {
            session.setAttribute("admin", admin.getUsername());
            return "redirect:/login/welcome-admin";
        }

        model.addAttribute("error", "Invalid credentials");
        return "login-admin";
    }

    // ----------- WELCOME PAGES -----------

    @GetMapping("/welcome-user")
    public String showWelcomeUser(HttpSession session,
                                  HttpServletRequest request,
                                  Model model) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return "redirect:/login/user";  // 🔐 Secure fallback
        }

        model.addAttribute("name", username);
        model.addAttribute("currentUrl", request.getRequestURI()); // For language switch
        return "welcome-user";
    }

    @GetMapping("/welcome-admin")
    public String showWelcomeAdmin(HttpSession session,
                                   HttpServletRequest request,
                                   Model model) {
        String username = (String) session.getAttribute("admin");
        if (username == null) {
            return "redirect:/login/admin";
        }

        model.addAttribute("name", username);
        model.addAttribute("currentUrl", request.getRequestURI());
        return "welcome-admin";
    }
}

package com.example.demo.controller;

import com.example.demo.dto.AdminDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Admin;
import com.example.demo.model.User;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Authentication API")
public class AuthApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register/user")
    public String registerUser(@Valid @RequestBody UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return "User registered successfully.";
    }

    @PostMapping("/register/admin")
    public String registerAdmin(@RequestBody AdminDTO dto) {
        Admin admin = new Admin();
        admin.setUsername(dto.username);
        admin.setEmail(dto.email);
        admin.setPassword(passwordEncoder.encode(dto.password));
        adminRepository.save(admin);
        return "Admin registered successfully.";
    }

    @PostMapping("/login/user")
    public String loginUser(@RequestBody UserDTO dto) {
        Optional<User> userOpt = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(dto.getUsername()) &&
                        passwordEncoder.matches(dto.getPassword(), u.getPassword()))
                .findFirst();

        return userOpt.isPresent() ? "User login successful." : "Invalid user credentials.";
    }

    @PostMapping("/login/admin")
    public String loginAdmin(@RequestBody AdminDTO dto) {
        Optional<Admin> adminOpt = adminRepository.findAll().stream()
                .filter(a -> a.getUsername().equals(dto.username) &&
                        passwordEncoder.matches(dto.password, a.getPassword()))
                .findFirst();

        return adminOpt.isPresent() ? "Admin login successful." : "Invalid admin credentials.";
    }
}

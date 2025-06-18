package com.example.demo.controller;

import com.example.demo.dto.UserMoodDTO;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moods")
public class UserMoodApiController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<UserMoodDTO> getAllUsersWithMoods() {
        return userRepository.findAll().stream()
                .map(user -> new UserMoodDTO(user.getUsername(), user.getMoodOfTheDay()))
                .toList();
    }
}

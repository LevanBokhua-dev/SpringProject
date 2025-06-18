package com.example.demo.service;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    public Todo saveTodoForUser(String username, Todo todo) {
        Optional<User> user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        if (user.isPresent()) {
            todo.setUser(user.get());
            return todoRepository.save(todo);
        }

        throw new RuntimeException("User not found");
    }
}

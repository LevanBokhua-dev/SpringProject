package com.example.demo.controller;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
public class TodoApiController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Todo>> getTodosByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            List<Todo> todos = todoRepository.findByUser(user.get());
            return ResponseEntity.ok(todos);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/new")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo saved = todoRepository.save(todo);
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoRepository.findAll());
    }

}

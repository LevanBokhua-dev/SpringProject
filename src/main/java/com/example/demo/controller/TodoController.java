package com.example.demo.controller;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/new")
    public String showTodoForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "todo-form";
    }

    @PostMapping("/new")
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo,
                          BindingResult result,
                          HttpSession session,
                          Model model) {

        if (result.hasErrors()) {
            return "todo-form";
        }

        String username = (String) session.getAttribute("user");
        Optional<User> user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();

        if (user.isPresent()) {
            todo.setUser(user.get());
            todoRepository.save(todo);
            return "redirect:/todo/list";
        }

        model.addAttribute("error", "User not found in session");
        return "todo-form";
    }

    @GetMapping("/list")
    public String listTodos(HttpSession session, Model model) {
        String username = (String) session.getAttribute("user");

        Optional<User> user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();

        if (user.isPresent()) {
            model.addAttribute("todos", todoRepository.findByUser(user.get()));
        } else {
            model.addAttribute("todos", List.of());
        }

        // ✅ Add the username to the model for the Thymeleaf page
        model.addAttribute("username", username);

        return "todo-list";
    }

}

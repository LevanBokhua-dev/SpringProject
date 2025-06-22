package com.example.demo.controller;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/new")
    public String showTodoForm(Model model) {
        logger.info("Displaying new todo form");
        model.addAttribute("todo", new Todo());
        return "todo-form";
    }

    @PostMapping("/new")
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo,
                          BindingResult result,
                          HttpSession session,
                          Model model) {

        if (result.hasErrors()) {
            logger.warn("Todo form has validation errors");
            return "todo-form";
        }

        String username = (String) session.getAttribute("user");
        logger.debug("Fetching user for username: {}", username);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            todo.setUser(user.get());
            todoRepository.save(todo);
            logger.info("New todo added for user: {}", username);
            return "redirect:/todo/list";
        }

        logger.error("User not found in session while adding todo");
        model.addAttribute("error", "User not found in session");
        return "todo-form";
    }

    @GetMapping("/all")
    public String listAllTodos(Model model) {
        logger.info("Listing all todos");
        model.addAttribute("todos", todoRepository.findAll());
        return "todo-list";
    }

    @GetMapping("/list")
    public String listTodos(HttpSession session, Model model) {
        String username = (String) session.getAttribute("user");
        logger.debug("Listing todos for user: {}", username);

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            model.addAttribute("todos", todoRepository.findByUser(user.get()));
            logger.info("Todos listed for user: {}", username);
        } else {
            model.addAttribute("todos", List.of());
            logger.warn("No user found in session during todo listing");
        }

        model.addAttribute("username", username);
        return "todo-list";
    }
}

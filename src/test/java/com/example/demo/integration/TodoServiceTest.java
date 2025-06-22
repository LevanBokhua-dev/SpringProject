package com.example.demo.integration;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveTodoForValidUser() {
        // given
        String username = "john";
        User user = new User();
        user.setUsername(username);

        Todo todo = new Todo();
        todo.setTitle("Test TODO");

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Todo savedTodo = todoService.saveTodoForUser(username, todo);

        assertThat(savedTodo.getUser()).isEqualTo(user);
        assertThat(savedTodo.getTitle()).isEqualTo("Test TODO");

        verify(todoRepository, times(1)).save(todo);
    }
}

package com.example.demo.integration;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoApiIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/todos/user";

        todoRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("123456");

        user = userRepository.save(user);

        Todo todo = new Todo();
        todo.setTitle("Integration Test");
        todo.setDescription("Testing GET todos");
        todo.setPriority("A");
        todo.setUser(user);

        todoRepository.save(todo);
    }

    @Test
    void shouldCreateTodoForUser() {
        User user = new User();
        user.setUsername("apitestuser");
        user.setEmail("api@example.com");
        user.setPassword("pass123");
        user = userRepository.save(user);

        String todoJson = String.format(
                "{\"title\":\"Test Via API\",\"description\":\"Created in test\",\"priority\":\"B\",\"user\":{\"id\":%d}}",
                user.getId()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(todoJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/todos/new", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).anyMatch(t -> t.getTitle().equals("Test Via API"));
    }
}

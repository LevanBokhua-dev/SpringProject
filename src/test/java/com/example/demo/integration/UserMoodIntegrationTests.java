package com.example.demo.integration;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserMoodIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/login/set-mood";
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("moodtester");
        user.setEmail("mood@test.com");
        user.setPassword("test123");
        userRepository.save(user);
    }

    @Test
    void shouldSaveMoodInDatabase() {
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> loginEntity = new HttpEntity<>("username=moodtester&password=test123", loginHeaders);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("http://localhost:" + port + "/login/user", loginEntity, String.class);

        String sessionCookie = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Cookie", sessionCookie);

        HttpEntity<String> request = new HttpEntity<>("mood=Joyful", headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/login/set-mood", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        Optional<User> updatedUser = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals("moodtester"))
                .findFirst();

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getMoodOfTheDay()).isEqualTo("Joyful");
    }

}

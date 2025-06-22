package com.example.demo.unit;

import com.example.demo.controller.RegistrationController;
import com.example.demo.model.Admin;
import com.example.demo.model.User;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Act
        String view = registrationController.registerUser(user, bindingResult, model);

        // Assert
        assertEquals("register-success", view);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
        verify(model).addAttribute("isAdmin", false);
    }

    @Test
    void registerUser_validationError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = registrationController.registerUser(new User(), bindingResult, model);

        assertEquals("register-user", view);
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerAdmin_success() {
        // Arrange
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("adminpass");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode("adminpass")).thenReturn("encodedAdminPass");

        // Act
        String view = registrationController.registerAdmin(admin, bindingResult, model);

        // Assert
        assertEquals("register-success", view);
        verify(passwordEncoder).encode("adminpass");
        verify(adminRepository).save(admin);
        verify(model).addAttribute("isAdmin", true);
    }

    @Test
    void registerAdmin_validationError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = registrationController.registerAdmin(new Admin(), bindingResult, model);

        assertEquals("register-admin", view);
        verify(adminRepository, never()).save(any());
    }
}

package org.example.cloudstorage.service;

import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAuthenticate_ValidCredentials() {
        String username = "test";
        String password = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        String authToken = authService.authenticate(username, password);

        assertThat(authToken).isNotNull();
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        // Given
        String username = "test";
        String password = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        String authToken = authService.authenticate(username, password);

        assertThat(authToken).isNull();
    }
}

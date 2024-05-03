package org.example.cloudstorage.service;

import org.example.cloudstorage.model.User;
import org.example.cloudstorage.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadUserByUsername_ExistingUsername_ReturnsUserDetails() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        when(userRepository.findByUsername("test")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("test");

        assertEquals("test", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    public void loadUserById_ExistingId_ReturnsUserDetails() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserById(1L);

        assertEquals("test", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    public void registerUser_ValidUser_UserRegisteredAndSavedToRepository() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void registerUser_ValidUser_PasswordEncodedBeforeSaving() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        userService.registerUser(user);

        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    public void registerUser_ValidUser_ReturnsRegisteredUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("encodedPassword", registeredUser.getPassword());
    }

    @Test
    public void updateUser_ExistingId_UserUpdatedAndSavedToRepository() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User resultUser = userService.updateUser(1L, updatedUser);

        assertEquals("newUsername", resultUser.getUsername());
        assertEquals("encodedNewPassword", resultUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void deleteUser_ExistingId_UserDeletedFromRepository() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}

package org.blog.blogapp.services;
import org.blog.blogapp.dto.UserProfileDTO;
import org.blog.blogapp.entities.User;
import org.blog.blogapp.exceptions.UserAlreadyExistsException;
import org.blog.blogapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() throws UserAlreadyExistsException {
        UserProfileDTO userProfileDTO = new UserProfileDTO("Stephen Muteti", "stevo@mail.com", "Jocteve1@","ADMIN_ROLE");
        User user = new User();
        user.setEmail(userProfileDTO.getEmail());
        when(userRepository.findByEmail(userProfileDTO.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(userProfileDTO.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.registerUser(userProfileDTO);

        verify(userRepository, times(1)).findByEmail(userProfileDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(userProfileDTO.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        UserProfileDTO userProfileDTO = new UserProfileDTO("Stephen Muteti", "mutetistephen21@gmail.com", "Jocteve1@","ADMIN_ROLE");
        User existingUser = new User();
        when(userRepository.findByEmail(userProfileDTO.getEmail())).thenReturn(existingUser);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userProfileDTO));
        verify(userRepository, times(1)).findByEmail(userProfileDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}

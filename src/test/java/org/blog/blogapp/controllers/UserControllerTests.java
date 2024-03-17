package org.blog.blogapp.controllers;

import org.blog.blogapp.dto.LoginDTO;
import org.blog.blogapp.dto.UserProfileDTO;
import org.blog.blogapp.exceptions.AuthorizationException;
import org.blog.blogapp.exceptions.UserAlreadyExistsException;
import org.blog.blogapp.exceptions.UserNotFoundException;
import org.blog.blogapp.services.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class UserControllerTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser() throws Exception, UserAlreadyExistsException {
        doNothing().when(userService).registerUser(any(UserProfileDTO.class));
        String requestBody = "{\"email\": \"test@example.com\", \"password\": \"password123\"}";
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\": \"User registered successfully\"}"));
    }

    @Test
    public void testLogin() throws Exception, UserNotFoundException {
        when(userService.login(any(LoginDTO.class))).thenReturn("dummyToken");
        String requestBody = "{\"email\": \"test@example.com\", \"password\": \"password123\"}";
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"dummyToken\"}"));
    }

    @Test
    public void testUpdateProfile() throws Exception, UserNotFoundException, AuthorizationException {
        doNothing().when(userService).editProfile(any(Long.class), any(UserProfileDTO.class));
        String requestBody = "{\"email\": \"test@example.com\", \"password\": \"newPassword123\"}";
        mockMvc.perform(put("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"Profile updated successfully\"}"));
    }
}

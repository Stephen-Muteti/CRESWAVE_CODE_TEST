package org.blog.blogapp.controllers;

import lombok.RequiredArgsConstructor;
import org.blog.blogapp.dto.LoginDTO;
import org.blog.blogapp.dto.UserProfileDTO;
import org.blog.blogapp.exceptions.AuthorizationException;
import org.blog.blogapp.exceptions.UserAlreadyExistsException;
import org.blog.blogapp.exceptions.UserNotFoundException;
import org.blog.blogapp.responseClasses.ApiResponse;
import org.blog.blogapp.responseClasses.AuthResponse;
import org.blog.blogapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /*User Registration endpoint*/
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserProfileDTO userProfileDTO) {
        try {
            userService.registerUser(userProfileDTO);
            return ResponseEntity.ok(new ApiResponse("User registered successfully"));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage()));
        }
    }

    /*User Login endpoint*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws UserNotFoundException {
        String token = userService.login(loginDTO);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /*Profile Update endpoint*/
    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> editProfile(@PathVariable Long userId, @RequestBody UserProfileDTO userProfileDTO) {
        try {
            userService.editProfile(userId, userProfileDTO);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage()));
        } catch (AuthorizationException e) {
            ApiResponse response = new ApiResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

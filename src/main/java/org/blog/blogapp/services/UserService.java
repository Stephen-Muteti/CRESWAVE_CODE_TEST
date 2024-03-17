package org.blog.blogapp.services;

import lombok.RequiredArgsConstructor;
import org.blog.blogapp.dto.LoginDTO;
import org.blog.blogapp.dto.UserProfileDTO;
import org.blog.blogapp.entities.User;
import org.blog.blogapp.exceptions.*;
import org.blog.blogapp.repositories.UserRepository;
import org.blog.blogapp.utils.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;

    public void registerUser(UserProfileDTO userDTO) throws UserAlreadyExistsException {

        /*Check that no user with the provided email is already registered*/
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        /*Create a user object with the provided information*/
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        /*Save the record to the database*/
        userRepository.save(user);
    }

    public String login(LoginDTO loginDTO) throws UserNotFoundException {

        /*Check whether the user exists in the database first*/
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        /*Verify if the provided password matches the stored hashed password*/
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new AuthenticationFailureException("Invalid credentials");
        }

        /*If the user provided correct credentials, proceed to create an
        authentication token and send it to the client*/
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getRole());

            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    loginDTO.getPassword(),
                    authorities
            );
            authenticatedUser.setDetails(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            return jwtTokenUtil.generateTokenUtil(userDetails);
        } catch (AuthenticationException e) {
            throw new AuthenticationFailureException("Invalid credentials");
        }
    }

    public void editProfile(Long userId, UserProfileDTO userProfileDTO) throws UserNotFoundException, AuthorizationException {

        /*Check whether the owner of the profile or
         and admin is the one attempting the update*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        if (!authenticatedUsername.equals(user.getEmail()) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AuthorizationException("You are not authorized to edit this profile");
        }

        /*If everything is okay then update the user record*/
        user.setName(userProfileDTO.getName());
        user.setEmail(userProfileDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userProfileDTO.getPassword()));
        userRepository.save(user);
    }
}

package org.blog.blogapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileDTO {
    /*The user's name
    * The validation is simply checking that the name was provided*/
    @NotBlank(message = "Name is required")
    private String name;

    /*Email of the user and the constraints*/
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    /*The password and its validation*/
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String password;

    /*The role of the user being registered (admin, user)*/
    @NotBlank(message = "Role is required")
    private String role;
}
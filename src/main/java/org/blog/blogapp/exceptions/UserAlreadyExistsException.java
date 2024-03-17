package org.blog.blogapp.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserAlreadyExistsException extends Throwable {
    @JsonProperty("message")
    private final String message;
}

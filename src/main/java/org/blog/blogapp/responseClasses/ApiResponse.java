package org.blog.blogapp.responseClasses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class ApiResponse {
    @JsonProperty("message")
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }
}

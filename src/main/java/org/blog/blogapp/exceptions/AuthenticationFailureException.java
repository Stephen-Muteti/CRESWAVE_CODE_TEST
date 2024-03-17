package org.blog.blogapp.exceptions;

public class AuthenticationFailureException extends AuthenticationException {
    public AuthenticationFailureException(String message) {
        super(message);
    }
}
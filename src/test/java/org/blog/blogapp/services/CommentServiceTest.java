package org.blog.blogapp.services;

import org.blog.blogapp.entities.Comment;
import org.blog.blogapp.entities.Post;
import org.blog.blogapp.entities.User;
import org.blog.blogapp.exceptions.PostNotFoundException;
import org.blog.blogapp.exceptions.UserNotFoundException;
import org.blog.blogapp.repositories.CommentRepository;
import org.blog.blogapp.repositories.PostRepository;
import org.blog.blogapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateComment() throws UserNotFoundException, PostNotFoundException {
        Post post = new Post();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        Comment comment = new Comment();
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        Comment savedComment = commentService.createComment(1L, new Comment());
        verify(postRepository).findById(1L);
        verify(userRepository).findByEmail(anyString());
        verify(commentRepository).save(any(Comment.class));
        assertEquals(comment, savedComment);
    }

    @Test
    void testGetCommentsByPost() {
        when(commentRepository.findByPostId(eq(1L), any(Pageable.class))).thenReturn(Collections.emptyList());
        List<Comment> comments = commentService.getCommentsByPost(1L, 0, 10);
        verify(commentRepository).findByPostId(eq(1L), any(Pageable.class));
        assertEquals(0, comments.size());
    }
}
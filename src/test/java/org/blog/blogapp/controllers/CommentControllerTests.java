package org.blog.blogapp.controllers;

import org.blog.blogapp.dto.CommentDTO;
import org.blog.blogapp.entities.Comment;
import org.blog.blogapp.exceptions.PostNotFoundException;
import org.blog.blogapp.exceptions.UserNotFoundException;
import org.blog.blogapp.services.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

public class CommentControllerTests {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;

    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    public void testCreateComment() throws Exception, UserNotFoundException, PostNotFoundException {
        when(commentService.createComment(any(Long.class), any(Comment.class))).thenReturn(new Comment());
        String requestBody = "{\"content\": \"This is a test comment\"}";
        mockMvc.perform(post("/api/comments/create/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateComment() throws Exception {
        when(commentService.updateComment(any(Long.class), any(Comment.class))).thenReturn(new Comment());
        String requestBody = "{\"content\": \"Updated comment content\"}";
        mockMvc.perform(put("/api/comments/update/456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/comments/delete/789"))
                .andExpect(status().isOk());
    }
}

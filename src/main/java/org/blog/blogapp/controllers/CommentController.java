package org.blog.blogapp.controllers;

import lombok.RequiredArgsConstructor;
import org.blog.blogapp.dto.CommentDTO;
import org.blog.blogapp.entities.Comment;
import org.blog.blogapp.exceptions.PostNotFoundException;
import org.blog.blogapp.exceptions.UserNotFoundException;
import org.blog.blogapp.responseClasses.ApiResponse;
import org.blog.blogapp.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /*Endpoint to create a comment for the given post*/
    @PostMapping("/create/{postId}")
    public ResponseEntity<ApiResponse> createComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) throws UserNotFoundException, PostNotFoundException {
        commentService.createComment(postId, commentDTO.toEntity());
        ApiResponse response = new ApiResponse("Comment created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*Endpoint to read the comments of a particular post
    * The postId is provided from the client side */
    @GetMapping("/read/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<Comment> comments = commentService.getCommentsByPost(postId,page,size);
        return ResponseEntity.ok(comments);
    }

    /*Endpoint to edit a comment
    * Just before the update process we check whether the
    * logged in/authenticated user owns the comment or is an admin*/
    @PutMapping("/update/{commentId}")
    @PreAuthorize("@commentService.isCommentOwnerOrAdmin(#commentId)")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO updatedCommentDTO) {
        commentService.updateComment(commentId, updatedCommentDTO.toEntity());
        ApiResponse response = new ApiResponse("Comment updated successfully");
        return ResponseEntity.ok(response);
    }

    /*Endpoint to delete a comment
     * Just before the deletion process we check whether the
     * logged in/authenticated user owns the comment or is an admin*/
    @DeleteMapping("/delete/{commentId}")
    @PreAuthorize("@commentService.isCommentOwnerOrAdmin(#commentId)")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        ApiResponse response = new ApiResponse("Comment deleted successfully");
        return ResponseEntity.ok(response);
    }
}

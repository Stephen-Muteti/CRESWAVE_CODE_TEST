package org.blog.blogapp.controllers;

import lombok.RequiredArgsConstructor;
import org.blog.blogapp.dto.PostDTO;
import org.blog.blogapp.entities.Post;
import org.blog.blogapp.exceptions.PostNotFoundException;
import org.blog.blogapp.responseClasses.ApiResponse;
import org.blog.blogapp.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /*Blog post creation endpoint*/
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createPost(@RequestBody PostDTO postDTO) {
        postService.createPost(postDTO.toEntity());
        ApiResponse response = new ApiResponse("Post created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*Read blog posts endpoint
    * The page number and the page size should come from the client
    * This reduces initial load times since posts are loaded in bits */
    @GetMapping("/read")
    public ResponseEntity<Page<Post>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.getAllPosts(page, size);
        return ResponseEntity.ok(posts);
    }

    /*Read a particular blog post*/
    @GetMapping("/read/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) throws PostNotFoundException {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    /*Update a particular blog post endpoint
    * Check to ensure that the owner of the post or
    * an admin is the one attempting the
    * update first using PreAuthorize and then proceed */
    @PutMapping("/update/{postId}")
    @PreAuthorize("@postService.isPostOwnerOrAdmin(#postId)")
    public ResponseEntity<ApiResponse> updatePost(@PathVariable Long postId, @RequestBody PostDTO updatedPostDTO) throws PostNotFoundException {
        postService.updatePost(postId, updatedPostDTO.toEntity());
        ApiResponse response = new ApiResponse("Post updated successfully");
        return ResponseEntity.ok(response);
    }

    /*Delete a particular blog post endpoint
     * Check to ensure that the owner of the post or an admin
     *  is the one attempting the deletion first using
     * PreAuthorize and then proceed */
    @DeleteMapping("/delete/{postId}")
    @PreAuthorize("@postService.isPostOwnerOrAdmin(#postId)")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        ApiResponse response = new ApiResponse("Post deleted successfully");
        return ResponseEntity.ok(response);
    }

    /*Blog posts search endpoint
    * Here the page number and the size is passed from the client side
    * */
    @GetMapping("/search")
    public ResponseEntity<Page<Post>> searchPosts(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Page<Post> posts = postService.searchPosts(keyword, page, size);
        return ResponseEntity.ok(posts);
    }
}
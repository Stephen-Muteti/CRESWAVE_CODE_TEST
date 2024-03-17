package org.blog.blogapp.services;

import lombok.RequiredArgsConstructor;
import org.blog.blogapp.entities.Comment;
import org.blog.blogapp.entities.Post;
import org.blog.blogapp.entities.User;
import org.blog.blogapp.exceptions.AuthenticationException;
import org.blog.blogapp.exceptions.PostNotFoundException;
import org.blog.blogapp.exceptions.UserNotFoundException;
import org.blog.blogapp.repositories.CommentRepository;
import org.blog.blogapp.repositories.PostRepository;
import org.blog.blogapp.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public Comment createComment(Long postId, Comment comment) throws UserNotFoundException, PostNotFoundException {
        /*Check whether the post still exists(it could have been deleted)*/
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + postId));

        /*Get the currently authenticated user*/
        String userEmail = getLoggedEmail();
        User user = userRepository.findByEmail(userEmail);
        if (user == null){
            throw new UserNotFoundException("User not found with Email: " + userEmail);
        }
        /*set the comment post and user and save the comment*/
        comment.setPost(post);
        comment.setUser(user);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return commentRepository.findByPostId(postId, pageable);
    }

    public Comment updateComment(Long commentId, Comment updatedComment) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + commentId));
        comment.setContent(updatedComment.getContent());
        /*This is returned for testing purposes
        * Check the CommentServiceTests for more*/
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }


    private String getLoggedEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("You are not Logged in");
        }
        return authentication.getName();
    }

    public boolean isCommentOwnerOrAdmin(Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String authenticatedUsername = authentication.getName();
        Comment comment = commentRepository.findById(commentId).orElse(null);

        /*If the comment is available and the authenticated user is the owner or
        * an admin*/
        return comment != null && (authenticatedUsername.equals(comment.getUser().getEmail()) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}

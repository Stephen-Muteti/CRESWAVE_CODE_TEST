package org.blog.blogapp.services;

import lombok.RequiredArgsConstructor;
import org.blog.blogapp.entities.Post;
import org.blog.blogapp.entities.User;
import org.blog.blogapp.exceptions.AuthenticationException;
import org.blog.blogapp.exceptions.PostNotFoundException;
import org.blog.blogapp.repositories.PostRepository;
import org.blog.blogapp.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    /*This is a utility function to check whether the currently authenticated user is the owner
    * of the post whose postId is given*/
    public boolean isPostOwnerOrAdmin(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String authenticatedUsername = authentication.getName();
        Post post = postRepository.findById(postId).orElse(null);

        /*If the post is available and the authenticated user is the owner or
         * an admin*/
        return post != null && (authenticatedUsername.equals(post.getBlogger().getEmail()) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    /*A utility function to get the email of the currently authenticated user*/
    private String getLoggedEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("You are not Logged in");
        }
        return authentication.getName();
    }

    public Post createPost(Post post) {
        /*Get the currently authenticated user and set them as the blogger
        * for the post*/
        User blogger = userRepository.findByEmail(getLoggedEmail());
        post.setBlogger(blogger);
        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAll(pageRequest);
    }

    public Post getPostById(Long id) throws PostNotFoundException {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + id));
    }

    public void updatePost(Long id, Post updatedPost) throws PostNotFoundException {
        /*Get the post and update the details
        * Save the updated post*/
        Post post = getPostById(id);
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Page<Post> searchPosts(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageRequest);
    }
}
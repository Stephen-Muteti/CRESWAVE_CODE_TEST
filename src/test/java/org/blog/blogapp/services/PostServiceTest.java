package org.blog.blogapp.services;

import org.blog.blogapp.entities.Post;
import org.blog.blogapp.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreatePost() {
        Post post = new Post();
        when(postRepository.save(any(Post.class))).thenReturn(post);
        Post savedPost = postService.createPost(post);
        assertEquals(post, savedPost);
    }

    @Test
    void testGetAllPosts() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(postRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(Collections.emptyList()));
        Page<Post> posts = postService.getAllPosts(0, 10);
        assertEquals(0, posts.getContent().size());
    }
}


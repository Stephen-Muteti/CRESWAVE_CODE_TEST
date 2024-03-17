package org.blog.blogapp.dto;
import lombok.Data;
import org.blog.blogapp.entities.Post;

@Data
public class PostDTO {
    private String title;
    private String content;

    public Post toEntity() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setContent(this.content);
        return post;
    }
}
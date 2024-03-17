package org.blog.blogapp.dto;
import lombok.*;
import org.blog.blogapp.entities.Comment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String content;

    public Comment toEntity() {
        Comment comment = new Comment();
        comment.setContent(this.content);
        return comment;
    }
}
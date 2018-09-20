package cc.jaxer.blog.entities;

import lombok.Data;

import java.util.Date;

@Data
public class CommentEntity
{
    private String id;
    private String content;
    private Date createAt;
}

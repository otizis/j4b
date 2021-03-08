package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("T_REPLY")
public class ReplyEntity
{
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * pageId 为空，表示不是谋篇文章的留言，是播客留言
     */
    private String pageId;
    private String ip;
    private String content;
    private Date createAt;
    private Integer status;

}

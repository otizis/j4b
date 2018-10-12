package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("T_TAG")
public class TagEntity
{
    private String id;
    private String tag;
}

package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class BlogInfoEntity
{
    private String title;
    private String logoUrl;
    private String desc;
}

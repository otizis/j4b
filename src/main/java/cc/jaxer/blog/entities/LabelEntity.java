package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("T_LABEL")
public class LabelEntity
{
    private String id;
    private String name;
}

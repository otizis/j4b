package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("T_CONFIG")
public class ConfigEntity
{
    private String code;
    private String v;
}

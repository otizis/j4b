package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("T_CONFIG")
public class ConfigEntity
{
    @TableId(type = IdType.INPUT)
    private String code;
    private String v;
}

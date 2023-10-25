package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("T_TEXT_DETECT")
public class TextDetectEntity
{
    @TableId(type = IdType.INPUT)
    private String word;
    /**
     * 0 未分类，1
     */
    private Integer type;
}

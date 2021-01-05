package cc.jaxer.blog.entities;

import cc.jaxer.blog.common.validation.Update;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
@TableName("T_PAGE")
public class PageEntity
{
    @TableId(type = IdType.INPUT)
    @NotBlank(groups = Update.class)
    private String id;
    private String title;
    private String content;
    private String desc;
    private Date createAt;
    private Date updateAt;
    private Integer status;
    private String bgUrl;
    @TableField(exist = false)
    private List<TagEntity> tagList;
}

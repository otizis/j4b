package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("T_PAGE")
public class PageEntity
{
    @TableId(type = IdType.INPUT)
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

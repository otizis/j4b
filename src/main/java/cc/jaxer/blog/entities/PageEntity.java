package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("T_PAGE")
public class PageEntity
{
    private String id;
    private String title;
    private String content;
    private Date createAt;
    private Date updateAt;
    private Integer status;
    @TableField(exist = false)
    private List<LabelEntity> labelList;
}

package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("T_EXTRACT")
public class ExtractEntity
{
    @TableId(type = IdType.UUID)
    private String id ;//  VARCHAR(32),
    /**
     * 类别: 1图片,2文章,3文本段落,4剪切板图片,5视频
     */
    private Integer type ;
    private String title ;//  VARCHAR(256),
    private String content ;//  TEXT comment '',
    private String sourceUrl ;//  VARCHAR(512),
    /**
     * 状态分类：0删除 1正常 2待办事项 10不公开
     */
    private Integer status ;//  TINYINT DEFAULT 1,
    private String memo ;//  VARCHAR(512),
    private Date createAt ;//  TIMESTAMP,
    private Date updateAt ;//  TIMESTAMP

    public static final  String STATUS = "status";
    public static final  String TITLE = "title";
    public static final  String CONTENT = "content";
    public static final  String MEMO = "memo";
}

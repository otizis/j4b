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
    private Integer type ;//  TINYINT comment '类别1图片2文章3文本段落4剪切板图片',
    private String title ;//  VARCHAR(256),
    private String content ;//  TEXT comment '',
    private String sourceUrl ;//  VARCHAR(512),
    private Integer status ;//  TINYINT DEFAULT 1,
    private String memo ;//  VARCHAR(512),
    private Date createAt ;//  TIMESTAMP,
    private Date updateAt ;//  TIMESTAMP

    public static final  String STATUS = "status";
}

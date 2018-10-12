package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("T_PAGE_TAG")
public class PageTagEntity
{
    private String pageId;
    private String tagId;

    public PageTagEntity()
    {
    }

    public PageTagEntity(String pageId, String tagId)
    {
        this.pageId = pageId;
        this.tagId = tagId;
    }
}

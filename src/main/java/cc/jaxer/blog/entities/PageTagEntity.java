package cc.jaxer.blog.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("T_PAGE_TAG")
public class PageTagEntity
{
    private String pageId;
    private String tagId;

    public static final String PAGE_ID = "page_id";
    public static final String TAG_ID = "tag_id";

    public PageTagEntity()
    {
    }

    public PageTagEntity(String pageId, String tagId)
    {
        this.pageId = pageId;
        this.tagId = tagId;
    }
}

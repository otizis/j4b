package cc.jaxer.blog.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlogInfoEntity
{
    private String title;
    private String logoUrl;
    private String desc;
    // 链接列表
    private List<LinkEntity> linkList = new ArrayList<>();
    // 底部列表
    private List<String> footerList = new ArrayList<>();
}

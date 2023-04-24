package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.PageEntity;
import cc.jaxer.blog.entities.PageTagEntity;
import cc.jaxer.blog.entities.TagEntity;
import cc.jaxer.blog.mapper.PageMapper;
import cc.jaxer.blog.mapper.PageTagMapper;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 博客文章的控制器
 */
@RestController
public class PageController
{
    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private PageTagMapper pageTagMapper;

    @RequestMapping("/page/save")
    @NeedLogin
    public R addOrUpdate(@RequestBody PageEntity page)
    {
        if(StringUtils.isNotBlank(page.getId())){
            updatePageTag(page);

            page.setUpdateAt(new Date());
            pageMapper.updateById(page);
            return R.ok("id",page.getId());
        }
        Date now = new Date();
        page.setCreateAt(now);
        page.setUpdateAt(now);
        page.setId(UUID.randomUUID().toString().replace("-", ""));
        String content = page.getContent();
        int len = 30;
        if(content.length() < len){
            page.setDesc(content);
        }else{
            page.setDesc(content.substring(0, len));
        }
        pageMapper.insert(page);
        updatePageTag(page);
        return R.ok("id",page.getId());
    }

    @RequestMapping("/page/append")
    @NeedLogin
    public R append(@RequestBody PageEntity page)
    {
        if(StrUtil.isBlank(page.getId())){
            return R.error();
        }
        PageEntity pageEntity = pageMapper.selectById(page.getId());
        String content1 = pageEntity.getContent();
        content1 += "<hr/>";
        content1 = content1 + page.getContent();
        pageEntity.setContent(content1);
        pageMapper.updateById(pageEntity);
        return R.ok();
    }

    private void updatePageTag(PageEntity page)
    {
        pageTagMapper.delete(new QueryWrapper<PageTagEntity>().eq("PAGE_ID",page.getId()));
        List<TagEntity> tagList = page.getTagList();
        if (CollectionUtils.isEmpty(tagList))
        {
            return;
        }
        for (TagEntity tagEntity : tagList)
        {
            PageTagEntity pageTagEntity = new PageTagEntity(page.getId(),tagEntity.getId());
            pageTagMapper.insert(pageTagEntity);
        }
    }


    @RequestMapping("/page/info")
    @NeedLogin
    public R info(@RequestBody PageEntity page)
    {
        PageEntity result = pageMapper.selectById(page.getId());
        return R.ok("page",result);
    }

    @RequestMapping("/page/del")
    @NeedLogin
    public R del(@RequestBody PageEntity page)
    {
        page.setStatus(0);
        page.setUpdateAt(new Date());
        pageMapper.updateById(page);
        return R.ok();
    }
}

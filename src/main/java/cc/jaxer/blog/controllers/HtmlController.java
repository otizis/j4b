package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.ConfigCodeEnum;
import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.entities.BlogInfoEntity;
import cc.jaxer.blog.entities.ConfigEntity;
import cc.jaxer.blog.entities.LinkEntity;
import cc.jaxer.blog.entities.PageEntity;
import cc.jaxer.blog.mapper.ConfigMapper;
import cc.jaxer.blog.mapper.PageMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HtmlController
{
    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private PageMapper pageMapper;

    @RequestMapping(path = {"/", "/index.html"})
    public String index(ModelMap modelMap, String pageNum)
    {
        // blog信息
        modelMap.put("blogInfo", getBlogInfo());

        // page列表
        int pageN = 1;
        if (NumberUtils.isDigits(pageNum))
        {
            pageN = Integer.parseInt(pageNum);
        }
        IPage<PageEntity> pageEntityIPage = pageMapper.selectPage(new Page<>(pageN, 5), new QueryWrapper<PageEntity>
                ().orderByDesc("create_at"));
        modelMap.put("pageList", pageEntityIPage.getRecords());
        modelMap.put("total", pageEntityIPage.getPages());
        modelMap.put("pNum", pageN);

        return "index";
    }

    private BlogInfoEntity getBlogInfo()
    {
        QueryWrapper<ConfigEntity> queryWrapper = new QueryWrapper<ConfigEntity>().likeRight("code", "hl_")
                                                                                  .or()
                                                                                  .likeRight("code", "footer_")
                                                                                  .or()
                                                                                  .likeRight("code", "blog_")
                                                                                  .orderByAsc("code");
        List<ConfigEntity> configEntities = configMapper.selectList(queryWrapper);


        BlogInfoEntity blogInfoEntity = new BlogInfoEntity();
        // 链接列表
        List<LinkEntity> linkList = blogInfoEntity.getLinkList();
        // 底部列表
        List<String> footerList = blogInfoEntity.getFooterList();

        for (ConfigEntity configEntity : configEntities)
        {
            String code = configEntity.getCode();
            String v = configEntity.getV();
            if (code.startsWith("hl_"))
            {
                LinkEntity link = new LinkEntity();
                link.setName(code.replace("hl_", ""));
                link.setLink(v);
                linkList.add(link);
            }
            else if (code.startsWith("footer_"))
            {
                footerList.add(v);
            }
            else if (ConfigCodeEnum.blog_title.toString().equals(code))
            {
                blogInfoEntity.setTitle(v);
            }
            else if (ConfigCodeEnum.blog_desc.toString().equals(code))
            {
                blogInfoEntity.setDesc(v);
            }
            else if (ConfigCodeEnum.blog_logo_url.toString().equals(code))
            {
                blogInfoEntity.setLogoUrl(v);
            }
        }
        return blogInfoEntity;
    }

    @RequestMapping(path = {"/page/{id}"})
    public String page(ModelMap modelMap, @PathVariable("id") String id)
    {
        // blog信息
        BlogInfoEntity blogInfo = getBlogInfo();
        modelMap.put("blogInfo", blogInfo);

        // page
        PageEntity pageEntity = pageMapper.selectById(id);
        modelMap.put("page", pageEntity);

        return "page";
    }

    @RequestMapping(path = {"/config.html"})
    @NeedLogin(isPage = true)
    public String config(ModelMap modelMap)
    {
        List<ConfigEntity> configEntities = configMapper.selectList(new QueryWrapper<ConfigEntity>().orderByAsc
                ("code"));
        modelMap.put("configList", configEntities);
        return "config";
    }

    @RequestMapping(path = {"/login.html"})
    public String login()
    {
        return "login";
    }

    @RequestMapping(path = {"/editPage.html"})
    @NeedLogin(isPage = true)
    public String editPage(ModelMap modelMap, String pageNum)
    {
        int pageN = 1;
        if (NumberUtils.isDigits(pageNum))
        {
            pageN = Integer.parseInt(pageNum);
        }
        IPage<PageEntity> page = pageMapper.selectPage(new Page<>(pageN, 5), new QueryWrapper<PageEntity>()
                .orderByDesc("create_at"));

        modelMap.put("page", page);
        modelMap.put("total", page.getPages());
        return "editPage";
    }

}

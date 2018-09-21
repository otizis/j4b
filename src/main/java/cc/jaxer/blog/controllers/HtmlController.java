package cc.jaxer.blog.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class HtmlController
{
    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private PageMapper pageMapper;

    @RequestMapping(path = {"/", "/index.html"})
    public String index(ModelMap modelMap,String pageNum)
    {
        List<ConfigEntity> configEntities = configMapper.selectList(new QueryWrapper<ConfigEntity>()
                .orderByAsc("code"));
        HashMap<String, String> map = new HashMap<>();
        for (ConfigEntity configEntity : configEntities)
        {
            map.put(configEntity.getCode(),configEntity.getV());
        }
        BlogInfoEntity blogInfoEntity = new BlogInfoEntity();
        blogInfoEntity.setTitle(map.get("blog_title"));
        blogInfoEntity.setDesc(map.get("blog_desc"));
        blogInfoEntity.setLogoUrl(map.get("blog_logo_url"));
        // blog信息
        modelMap.put("blogInfo", blogInfoEntity);

        // page列表
        int pageN = 1;
        if (NumberUtils.isDigits(pageNum))
        {
            pageN = Integer.parseInt(pageNum);
        }
        IPage<PageEntity> pageEntityIPage = pageMapper.selectPage(new Page<>(pageN,5),
                new QueryWrapper<PageEntity>().orderByDesc("create_at"));
        modelMap.put("pageList", pageEntityIPage.getRecords());
        modelMap.put("total", pageEntityIPage.getPages());
        modelMap.put("pNum", pageN);

        // 链接列表
        List<LinkEntity> linkList = new ArrayList<>();
        for (ConfigEntity configEntity : configEntities)
        {
            if(configEntity.getCode().startsWith("hl_")){
                LinkEntity link = new LinkEntity();
                link.setName(configEntity.getCode().replace("hl_",""));
                link.setLink(configEntity.getV());
                linkList.add(link);
            }
        }
        modelMap.put("linkList", linkList);
        // 底部列表
        List<String> footerList = new ArrayList<>();
        for (ConfigEntity configEntity : configEntities)
        {
            if(configEntity.getCode().startsWith("footer_")){
                footerList.add(configEntity.getV());
            }
        }
        modelMap.put("footerList", footerList);
        return "index";
    }


    @RequestMapping(path = {"/page.html"})
    public String page(ModelMap modelMap,String id)
    {
        List<ConfigEntity> configEntities = configMapper.selectList(new QueryWrapper<ConfigEntity>()
                .orderByAsc("code"));
        HashMap<String, String> map = new HashMap<>();
        for (ConfigEntity configEntity : configEntities)
        {
            map.put(configEntity.getCode(),configEntity.getV());
        }
        BlogInfoEntity blogInfoEntity = new BlogInfoEntity();
        blogInfoEntity.setTitle(map.get("blog_title"));
        blogInfoEntity.setDesc(map.get("blog_desc"));
        blogInfoEntity.setLogoUrl(map.get("blog_logo_url"));
        // blog信息
        modelMap.put("blogInfo", blogInfoEntity);

        // page
        PageEntity pageEntity = pageMapper.selectById(id);
        modelMap.put("page", pageEntity);

        // 链接列表
        List<LinkEntity> linkList = new ArrayList<>();
        for (ConfigEntity configEntity : configEntities)
        {
            if(configEntity.getCode().startsWith("hl_")){
                LinkEntity link = new LinkEntity();
                link.setName(configEntity.getCode().replace("hl_",""));
                link.setLink(configEntity.getV());
                linkList.add(link);
            }
        }

        modelMap.put("linkList", linkList);
        // 底部列表
        List<String> footerList = new ArrayList<>();
        for (ConfigEntity configEntity : configEntities)
        {
            if(configEntity.getCode().startsWith("footer_")){
                footerList.add(configEntity.getV());
            }
        }
        modelMap.put("footerList", footerList);
        return "page";
    }

    @RequestMapping(path = {"/config.html"})
    @NeedLogin(isPage = true)
    public String config(ModelMap modelMap)
    {
        List<ConfigEntity> configEntities =
                configMapper.selectList(new QueryWrapper<ConfigEntity>()
                .orderByAsc("code"));
        modelMap.put("configList", configEntities);
        return "config";
    }

    @RequestMapping(path = {"/login.html"})
    public String login()
    {
        return "login";
    }


}

package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.entities.*;
import cc.jaxer.blog.mapper.*;
import cc.jaxer.blog.services.ConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HtmlController implements ErrorController
{
    private static final String ERROR_PATH = "/error";

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private PageTagMapper pageTagMapper;


    @RequestMapping(path = {"/", "/index.html"})
    public String index(ModelMap modelMap, String pageNum)
    {
        // blog信息
        modelMap.put("blogInfo", configService.getBlogInfo());

        // page列表
        int pageN = 1;
        if (NumberUtils.isDigits(pageNum))
        {
            pageN = Integer.parseInt(pageNum);
        }
        IPage<PageEntity> pageEntityIPage = pageMapper.selectPage(new Page<>(pageN, 27), new QueryWrapper<PageEntity>
                ().eq("status",1).orderByDesc("create_at"));
        modelMap.put("pageList", pageEntityIPage.getRecords());
        modelMap.put("total", pageEntityIPage.getPages());
        modelMap.put("pNum", pageN);

        return "index";
    }


    @RequestMapping(path = {"/pageFilter/tag/{tagId}"})
    public String pageFilter(ModelMap modelMap,  String pageNum, @PathVariable String tagId)
    {
        // blog信息
        modelMap.put("blogInfo", configService.getBlogInfo());

        // page列表
        int pageN = 1;
        if (NumberUtils.isDigits(pageNum))
        {
            pageN = Integer.parseInt(pageNum);
        }

        TagEntity tag = tagMapper.selectById(tagId);
        if(tag == null)
        {
            return "redict:/error";
        }
        modelMap.put("tag",tag);

        IPage<PageEntity> pageEntityIPage = pageMapper.selectPage(new Page<>(pageN, 27),
         new QueryWrapper<PageEntity>()
         .eq("status",1)
         .exists(" select 1 from T_PAGE_TAG where T_PAGE.ID = T_PAGE_TAG.PAGE_ID AND T_PAGE_TAG.TAG_ID = '" + tagId +"'")
         .orderByDesc("create_at")
         );
        modelMap.put("pageList", pageEntityIPage.getRecords());
        modelMap.put("total", pageEntityIPage.getPages());
        modelMap.put("pNum", pageN);

        return "pageFilter";
    }

    @Override
    public String getErrorPath()
    {
        return ERROR_PATH;
    }

    @RequestMapping(path = {ERROR_PATH})
    public String error(ModelMap modelMap)
    {
        // blog信息
        modelMap.put("blogInfo", configService.getBlogInfo());
        return "error";
    }

    @RequestMapping(path = {"/page/{id}"})
    public String page(ModelMap modelMap, @PathVariable("id") String id)
    {
        // blog信息
        BlogInfoEntity blogInfo = configService.getBlogInfo();
        modelMap.put("blogInfo", blogInfo);

        // page
        PageEntity pageEntity = pageMapper.selectById(id);
        if (pageEntity == null)
        {
            return "redirect:/error";
        }

        List<TagEntity> tagEntities = tagMapper.selectList(new QueryWrapper<>());
        List<PageTagEntity> pageTagR = pageTagMapper.selectList(new QueryWrapper<PageTagEntity>().eq("PAGE_ID", id));
        for (PageTagEntity pageTagEntity : pageTagR)
        {
            for (TagEntity tagEntity : tagEntities)
            {
                if(StringUtils.equals(tagEntity.getId(),pageTagEntity.getTagId())){
                    List<TagEntity> tagList = pageEntity.getTagList();
                    if(tagList==null){
                        tagList = new ArrayList<>();
                        pageEntity.setTagList(tagList);
                    }
                    tagList.add(tagEntity);
                }
            }
        }

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
        return "admin/config";
    }

    @RequestMapping(path = {"/login.html"})
    public String login()
    {
        return "login";
    }



    @RequestMapping(path = {"/editPageList.html"})
    @NeedLogin(isPage = true)
    public String editPageList(ModelMap modelMap, String pageNum)
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
        return "admin/editPageList";
    }

    @RequestMapping(path = {"/editPageContent.html"})
    @NeedLogin(isPage = true)
    public String editPageContent(ModelMap modelMap, String id)
    {
        PageEntity pageEntity = pageMapper.selectById(id);
        modelMap.put("pageEntity",pageEntity);

        List<PageTagEntity> pageTagEntities = pageTagMapper.selectList(new QueryWrapper<PageTagEntity>().eq("PAGE_ID",id));
        if(!CollectionUtils.isEmpty(pageTagEntities))
        {
            List<String> tagIdList = new ArrayList<>();
            for (PageTagEntity tagEntity : pageTagEntities)
            {
                String tagId = tagEntity.getTagId();
                tagIdList.add(tagId);
            }
            modelMap.put("tagIdList",tagIdList);
        }


        List<TagEntity> tagEntities = tagMapper.selectList(new QueryWrapper<>());
        modelMap.put("tagEntities",tagEntities);

        return "admin/editPageContent";
    }


    @RequestMapping(path = {"/editReply.html"})
    @NeedLogin(isPage = true)
    public String editReply(ModelMap modelMap, String pageNum)
    {
        int pageN = 1;
        if (NumberUtils.isDigits(pageNum))
        {
            pageN = Integer.parseInt(pageNum);
        }
        IPage<ReplyEntity> replyPage = replyMapper.selectPage(new Page<>(pageN, 10), new QueryWrapper<ReplyEntity>()
                .orderByDesc("create_at"));

        modelMap.put("page", replyPage);
        modelMap.put("total", replyPage.getPages());
        return "admin/editReply";
    }


    @RequestMapping(path = {"/editTagList.html"})
    @NeedLogin(isPage = true)
    public String editTagList(ModelMap modelMap)
    {
        List<TagEntity> list = tagMapper.selectList(new QueryWrapper<TagEntity>());

        modelMap.put("tagList", list);
        return "admin/editTagList";
    }


}

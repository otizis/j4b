package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.AppConstant;
import cc.jaxer.blog.common.ConfigCodeEnum;
import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.entities.*;
import cc.jaxer.blog.mapper.*;
import cc.jaxer.blog.services.ConfigService;
import cc.jaxer.blog.services.PageService;
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
    private PageService pageService;

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

        String conf = configService.getConfDefault(ConfigCodeEnum.unsplash_proxy, AppConstant.UNSPLASH_DOMAIN);
        modelMap.put("unsplashDomain", conf);

        String bgmUrl = configService.getConf(ConfigCodeEnum.bgm_url);
        if(StringUtils.isNotBlank(bgmUrl)){
            modelMap.put("bgmUrl", bgmUrl);
        }

        // page列表
        int pageN = 1;
        if (NumberUtils.isDigits(pageNum))
        {
            pageN = Integer.parseInt(pageNum);
        }
        QueryWrapper<PageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1).orderByDesc("create_at");
        IPage<PageEntity> pageEntityIPage = pageService.page(new Page<>(pageN, 5), queryWrapper);
        modelMap.put("pageList", pageEntityIPage.getRecords());


        TagEntity tagEntity = tagMapper.selectOne(new QueryWrapper<TagEntity>()
                .eq("tag", "置顶").or().eq("tag", "top"));
        if(tagEntity!=null){
            IPage<PageEntity> topPageList = pageService.getPageListByTag(tagEntity.getId(), new Page<>(pageN, 3));
            modelMap.put("topPageList", topPageList.getRecords());
        }

        modelMap.put("total", pageEntityIPage.getPages());
        modelMap.put("pNum", pageN);

        return "index";
    }


    @RequestMapping(path = {"/pageFilter/tag/{tagId}"})
    public String pageFilter(ModelMap modelMap,  String pageNum, @PathVariable String tagId)
    {

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

        IPage<PageEntity> pageEntityIPage = pageService.getPageListByTag(tagId,new Page<>(pageN, 27));
        modelMap.put("pageList", pageEntityIPage.getRecords());
        modelMap.put("total", pageEntityIPage.getPages());
        modelMap.put("pNum", pageN);

        return "pageFilter";
    }


    @RequestMapping(path = {"/pageFilter/search"})
    public String pageFilterByKeyword(ModelMap modelMap,  String pageNum, String keyword)
    {
        // page列表
        int pageN = 1;
        if (NumberUtils.isDigits(pageNum))
        {
            pageN = Integer.parseInt(pageNum);
        }
        modelMap.put("keyword",keyword);

        IPage<PageEntity> pageEntityIPage = pageService.page(new Page<>(pageN, 27),
         new QueryWrapper<PageEntity>()
         .eq("status",1)
         .like("content",keyword)
         .orderByDesc("create_at")
         );
        modelMap.put("pageList", pageEntityIPage.getRecords());
        modelMap.put("total", pageEntityIPage.getPages());
        modelMap.put("pNum", pageN);

        return "pageSearch";
    }

    @RequestMapping(path = {"/reply/page"})
    public String replayPage(ModelMap modelMap, String pageNum)
    {
        // page列表
        int pageN = 1;
        if (NumberUtils.isDigits(pageNum))
        {
            pageN = Integer.parseInt(pageNum);
        }

        IPage<ReplyEntity> replyPage = replyMapper.selectPage(new Page<>(pageN, 27), new
                QueryWrapper<ReplyEntity>()
                .eq("status",1)
                .orderByDesc("create_at"));

        modelMap.put("replyList", replyPage.getRecords());
        modelMap.put("total", replyPage.getPages());
        modelMap.put("pNum", pageN);

        return "replyPage";
    }

    @Override
    public String getErrorPath()
    {
        return ERROR_PATH;
    }

    @RequestMapping(path = {ERROR_PATH})
    public String error(ModelMap modelMap)
    {
        return "error";
    }

    @RequestMapping(path = {"/page/{id}"})
    public String page(ModelMap modelMap, @PathVariable("id") String id)
    {


        String conf = configService.getConfDefault(ConfigCodeEnum.unsplash_proxy, AppConstant.UNSPLASH_DOMAIN);
        modelMap.put("unsplashDomain", conf);

        // page
        PageEntity pageEntity = pageService.getById(id);
        if (pageEntity == null)
        {
            return "redirect:/error";
        }
        List<TagEntity> tagListByPageId = pageService.getTagListByPageId(id);
        pageEntity.setTagList(tagListByPageId);

        modelMap.put("page", pageEntity);
        return "page";
    }

    @RequestMapping(path = {"/config.html"})
    @NeedLogin(isPage = true)
    public String config(ModelMap modelMap)
    {
        QueryWrapper<ConfigEntity> code = new QueryWrapper<ConfigEntity>().orderByAsc("code");
        List<ConfigEntity> configEntities = configMapper.selectList(code);
        modelMap.put("configList", configEntities);

        ConfigCodeEnum[] supportConfigList = ConfigCodeEnum.values();

        modelMap.put("supportConfigList", supportConfigList);

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
        IPage<PageEntity> page = pageService.page(new Page<>(pageN, 5), new QueryWrapper<PageEntity>()
                .orderByDesc("create_at"));

        modelMap.put("page", page);
        modelMap.put("total", page.getPages());
        return "admin/editPageList";
    }

    @RequestMapping(path = {"/editPageContent.html"})
    @NeedLogin(isPage = true)
    public String editPageContent(ModelMap modelMap, String id)
    {
        PageEntity pageEntity = pageService.getById(id);
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

    @RequestMapping(path = {"/export.html"})
    @NeedLogin(isPage = true)
    public String export(ModelMap modelMap)
    {
        return "admin/export";
    }


}

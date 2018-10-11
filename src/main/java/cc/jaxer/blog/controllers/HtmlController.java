package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.entities.BlogInfoEntity;
import cc.jaxer.blog.entities.ConfigEntity;
import cc.jaxer.blog.entities.PageEntity;
import cc.jaxer.blog.entities.ReplyEntity;
import cc.jaxer.blog.mapper.ConfigMapper;
import cc.jaxer.blog.mapper.PageMapper;
import cc.jaxer.blog.mapper.ReplyMapper;
import cc.jaxer.blog.services.ConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HtmlController implements ErrorController
{
    private static final String PATH = "/error";

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private ConfigService configService;


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
        IPage<PageEntity> pageEntityIPage = pageMapper.selectPage(new Page<>(pageN, 5), new QueryWrapper<PageEntity>
                ().orderByDesc("create_at"));
        modelMap.put("pageList", pageEntityIPage.getRecords());
        modelMap.put("total", pageEntityIPage.getPages());
        modelMap.put("pNum", pageN);

        return "index";
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

    @RequestMapping(path = {PATH})
    public String error(ModelMap modelMap)
    {
        // blog信息
        modelMap.put("blogInfo", configService.getBlogInfo());
        return "error";
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
        return "editReply";
    }

    @Override
    public String getErrorPath()
    {
        return PATH;
    }
}

package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.J4bUtils;
import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.BlogInfoEntity;
import cc.jaxer.blog.entities.ConfigEntity;
import cc.jaxer.blog.mapper.ConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class SysController
{
    @Autowired
    private ConfigMapper configMapper;

    @RequestMapping("/sys/login")
    public R login(@RequestBody HashMap<String, String> request,HttpServletResponse response)
    {
        String access = request.get("access");
        ConfigEntity conf = configMapper.selectById("access");
        if (!StringUtils.equals(access, conf==null?"j4bj4b":conf.getV()))
        {
            return R.error(500,"密码错误");
        }
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, 1);
        J4bUtils.addToken(token, now.getTimeInMillis());
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);
        return R.ok("token", token);
    }


    @RequestMapping("/sys/updateBlogInfo")
    @NeedLogin
    public R updateBlogInfo(@RequestBody BlogInfoEntity entity)
    {
        String title = entity.getTitle();
        if (!StringUtils.isEmpty(title))
        {
            ConfigEntity entity1 = new ConfigEntity();
            entity1.setCode("blog_title");
            entity1.setV(title);
            configMapper.updateById(entity1);
        }
        String desc = entity.getDesc();
        if (!StringUtils.isEmpty(title))
        {
            ConfigEntity entity1 = new ConfigEntity();
            entity1.setCode("blog_desc");
            entity1.setV(desc);
            configMapper.updateById(entity1);
        }
        String logoUrl = entity.getLogoUrl();
        if (!StringUtils.isEmpty(title))
        {
            ConfigEntity entity1 = new ConfigEntity();
            entity1.setCode("blog_logo_url");
            entity1.setV(logoUrl);
            configMapper.updateById(entity1);
        }
        return R.ok();
    }

    @RequestMapping("/sys/config/update")
    @NeedLogin
    public R updateConfig(@RequestBody ConfigEntity entity)
    {
        int i = configMapper.updateById(entity);
        if (i == 0)
        {
            configMapper.insert(entity);
        }
        return R.ok();
    }

    @RequestMapping("/sys/config/delete")
    @NeedLogin
    public R deleteConfig(@RequestBody ConfigEntity entity)
    {
        int i = configMapper.deleteById(entity.getCode());
        return R.ok();
    }

    @RequestMapping("/sys/check")
    @NeedLogin
    public R check()
    {
        return R.ok();
    }
}

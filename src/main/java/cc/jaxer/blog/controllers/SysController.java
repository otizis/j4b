package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.*;
import cc.jaxer.blog.entities.ConfigEntity;
import cc.jaxer.blog.mapper.ConfigMapper;
import cc.jaxer.blog.services.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class SysController
{
    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ConfigService configService;

    @RequestMapping("/sys/login")
    public R login(@RequestBody HashMap<String, String> request,HttpServletResponse response)
    {
        String access = request.get(ConfigCodeEnum.access.toString());
        ConfigEntity conf = configMapper.selectById(ConfigCodeEnum.access.toString());
        if (!StringUtils.equals(access, conf==null?"j4bj4b":conf.getV()))
        {
            return R.error(500,"密码错误");
        }
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, 1);
        J4bUtils.addToken(token, now.getTimeInMillis());
        Cookie cookie = new Cookie(AppConstant.HEADER_TOKEN_KEY, token);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
        return R.ok(AppConstant.HEADER_TOKEN_KEY, token).put("from",request.get("from"));
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
        configService.reloadBlogInfo();
        return R.ok();
    }

    @RequestMapping("/sys/config/delete")
    @NeedLogin
    public R deleteConfig(@RequestBody ConfigEntity entity)
    {
        configMapper.deleteById(entity.getCode());
        return R.ok();
    }

    @RequestMapping("/sys/check")
    @NeedLogin
    public R check()
    {
        return R.ok();
    }
}

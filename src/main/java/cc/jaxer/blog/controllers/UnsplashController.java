package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.R;
import cc.jaxer.blog.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnsplashController
{
    @Autowired
    private ConfigMapper configMapper;

    @ResponseBody
    public R searchImage(String keyword){
        return R.ok();
    }
}

package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.services.UnsplashService;
import cc.jaxer.blog.unsplash.SearchReq;
import cc.jaxer.blog.unsplash.SearchResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class UnsplashController
{
    @Autowired
    UnsplashService unsplashService;

    @RequestMapping("/unsplash/list")
    @NeedLogin
    public String searchImage(ModelMap modelMap, SearchReq searchReq)
    {
        modelMap.addAttribute("searchReq",searchReq);

        if(StringUtils.isBlank(searchReq.getQuery())){
            modelMap.addAttribute("resp",new SearchResp());
            return "unsplash/list";
        }

        SearchResp resp = unsplashService.search(searchReq);
        if(resp == null){
            return "unsplash/unset";
        }
        modelMap.addAttribute("resp",resp);
        return "unsplash/list";
    }
}

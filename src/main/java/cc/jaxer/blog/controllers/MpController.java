package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.R;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class MpController
{
    @Autowired
    private Environment env;

    @RequestMapping(path = {"/mp/login/{mpcode}/{code}"})
    @ResponseBody
    public R miniFont(@PathVariable String mpcode,
                      @PathVariable String code)
    {
        String appid = env.getProperty("mp."+mpcode+".appid");
        String appsecret = env.getProperty("mp."+mpcode+".appsecret");
        if(StringUtils.isEmpty(appid) || StringUtils.isEmpty(appsecret)){
            return R.error();
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=${APPID}&secret=${SECRET}&js_code=${JSCODE}&grant_type=authorization_code";
        url = url.replace("${JSCODE}", code)
                .replace("${APPID}",appid)
                 .replace("${SECRET}",appsecret);

        String resp = HttpUtil.get(url);
        /**
         * {
         * "openid":"xxxxxx",
         * "session_key":"xxxxx",
         * "unionid":"xxxxx",
         * "errcode":0,
         * "errmsg":"xxxxx"
         * }
         */
        if(StringUtils.isEmpty(resp)){
            return R.error();
        }
        JSONObject parse = JSONUtil.parseObj(resp);
        String openid = parse.getStr("openid");
        if(StringUtils.isEmpty(openid)){
            return R.error(resp);
        }
        return R.ok().put("openid",openid);
    }
}

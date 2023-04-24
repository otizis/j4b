package cc.jaxer.blog.configs;

import cc.jaxer.blog.common.*;
import cc.jaxer.blog.services.ConfigService;
import cn.hutool.crypto.digest.MD5;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class NeedAuthAop
{
    @Autowired
    private ConfigService configService;

    private String authToken;


    @Pointcut("@annotation(cc.jaxer.blog.common.NeedAuth)")
    public void dataFilterCut()
    {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable
    {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String token = request.getHeader(AppConstant.HEADER_AUTH_KEY);

        if (checkToken(token))
        {
            return;
        }
        throw new BException("ill");
    }

    private boolean checkToken(String token)
    {
        if(token == null){
            return false;
        }
        String setStr = getAuthString();
        if(setStr == null){
            return true;
        }
        return MD5.create().digestHex16(setStr).equals(token);
    }

    public String getAuthString()
    {
        if(authToken != null){
            return authToken;
        }
        String conf = configService.getConf(ConfigCodeEnum.access);
        if(conf != null){
            authToken = conf;
        }
        if(authToken == null){
            return "j4bj4b";
        }
        return authToken;
    }
}

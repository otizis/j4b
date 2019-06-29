package cc.jaxer.blog.configs;

import cc.jaxer.blog.common.AppConstant;
import cc.jaxer.blog.common.BException;
import cc.jaxer.blog.common.J4bUtils;
import cc.jaxer.blog.common.NeedLogin;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Aspect
@Component
public class NeedLoginAop
{
    @Pointcut("@annotation(cc.jaxer.blog.common.NeedLogin)")
    public void dataFilterCut()
    {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable
    {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        String token = request.getHeader(AppConstant.HEADER_TOKEN_KEY);
        if (StringUtils.isEmpty(token))
        {
            Cookie[] cookies = request.getCookies();
            if (cookies != null)
            {
                for (Cookie cookie : cookies)
                {
                    if (StringUtils.equals(cookie.getName(), AppConstant.HEADER_TOKEN_KEY))
                    {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (J4bUtils.checkToken(token))
        {
            return;
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        NeedLogin needLogin = signature.getMethod().getAnnotation(NeedLogin.class);
        if (needLogin.isPage())
        {
            response.sendRedirect("/login.html?"+URLEncoder.encode(request.getRequestURL() +"?"+ request.getQueryString()));
            return;
        }
        throw new BException("未登录");
    }
}

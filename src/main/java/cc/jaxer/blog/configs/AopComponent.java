package cc.jaxer.blog.configs;

import cc.jaxer.blog.common.BException;
import cc.jaxer.blog.common.J4bUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AopComponent
{
    @Pointcut("@annotation(cc.jaxer.blog.common.NeedLogin)")
    public void dataFilterCut()
    {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable
    {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        String token = request.getHeader("token");
        if (J4bUtils.checkToken(token))
        {
            return;
        }
        throw new BException("未登录");
    }
}

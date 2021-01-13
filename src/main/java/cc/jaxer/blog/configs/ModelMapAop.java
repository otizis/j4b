package cc.jaxer.blog.configs;

import cc.jaxer.blog.services.ConfigService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

@Aspect
@Component
public class ModelMapAop
{
    @Autowired
    private ConfigService configService;

    /**
     * 返回string的controller方法，认为是网页，需要注入博客信息
     */
    @Pointcut("execution(public String cc.jaxer.blog.controllers.*.*(..))")
    private void pointcut() {

    }

    @Before("pointcut()")
    public void dealSearchDate(JoinPoint joinpoint) {
        Object[] args = joinpoint.getArgs();
        for (Object arg : args)
        {
            if(arg instanceof ModelMap){
                ModelMap modelMap = (ModelMap) arg;
                modelMap.put("blogInfo", configService.getBlogInfo());
                break;
            }
        }
    }
}

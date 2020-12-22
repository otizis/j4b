package cc.jaxer.blog;

import cc.jaxer.blog.common.PermissionTagDirective;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
@MapperScan("cc.jaxer.blog.mapper")
public class BlogApplication
{
    @Autowired
    Configuration configuration;

    @Autowired
    PermissionTagDirective permissionTagDirective;

    @PostConstruct //在项目启动时执行方法
    public void setSharedVariable()
    {
        // 将标签perm注册到配置文件
        configuration.setSharedVariable("hasLogin", permissionTagDirective);

    }
    public static void main(String[] args)
    {
        SpringApplication.run(BlogApplication.class, args);
    }
}

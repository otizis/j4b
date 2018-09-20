package cc.jaxer.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cc.jaxer.blog.mapper")
public class BlogApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(BlogApplication.class, args);
    }
}

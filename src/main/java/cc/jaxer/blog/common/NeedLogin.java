package cc.jaxer.blog.common;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedLogin
{
    boolean isPage() default false;
}

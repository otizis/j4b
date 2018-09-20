package cc.jaxer.blog.configs;

import cc.jaxer.blog.common.BException;
import cc.jaxer.blog.common.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BExceptionHandler
{
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BException.class)
    public R handleRRException(BException e)
    {
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
        return r;
    }
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable e)
    {
        R r = new R();
        r.put("code", 500);
        r.put("msg", e.getMessage());
        return r;
    }
}

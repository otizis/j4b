package cc.jaxer.blog.common;

import cc.jaxer.blog.common.J4bUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * freemarker 增加自定义标签
 */
@Component
public class PermissionTagDirective implements TemplateDirectiveModel
{

    @Autowired
    private HttpServletRequest request;

    private final static String URL = "url";

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException
    {
        if (J4bUtils.isLogin())
        {
            body.render(env.getOut());
        }
    }

}
package cc.jaxer.blog.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;

public class J4bUtils
{
    /**
     * token池
     */
    static ConcurrentHashMap<String, Long> tokenMap = new ConcurrentHashMap<>();

    public static void addToken(String token, long timeInMillis)
    {
        tokenMap.put(token, timeInMillis);
    }

    public static boolean checkToken(String token)
    {
        if(token == null){
            return false;
        }
        Long aLong = tokenMap.get(token);
        if(aLong == null){
            return false;
        }
        if(System.currentTimeMillis() > aLong){
            tokenMap.remove(token);
            return false;
        }
        return true;
    }

    private static String getCurrToken()
    {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes)requestAttributes).getRequest();
        String token = req.getHeader(AppConstant.HEADER_TOKEN_KEY);
        if (StringUtils.isEmpty(token))
        {
            Cookie[] cookies = req.getCookies();
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
        return token;
    }

    public static boolean  isLogin(){
        return checkToken(getCurrToken());
    }
}

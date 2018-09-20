package cc.jaxer.blog.common;

import java.util.concurrent.ConcurrentHashMap;

public class J4bUtils
{
    /**
     * token池
     */
    static ConcurrentHashMap<String, Long> tokenMap = new ConcurrentHashMap();

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
}

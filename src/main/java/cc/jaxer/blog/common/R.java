package cc.jaxer.blog.common;

import java.util.HashMap;

public class R extends HashMap<String,Object>
{
    private int code = 0;
    private String msg ;

    public static R error(int code, String msg){
        R r = new R();
        r.code = code;
        r.msg = msg;
        return r;
    }
    public static R error(){
        return error(500,"服务器错误");
    }
    public static R ok(){
        return new R();
    }
    public static R ok(String key,Object obj){
        R r = new R();
        r.put(key, obj);
        return r;
    }

    @Override
    public R put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }
}

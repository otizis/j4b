package cc.jaxer.blog.common;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class R extends HashMap<String,Object>
{
    public static R error(int code, String msg){
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }
    public static R error(){
        return error(500,"服务器错误");
    }
    public static R ok(){
        R r = new R();
        r.put("code", 0);
        return r;
    }
    public static R ok(String key,Object obj){
        R r = new R();
        r.put("code", 0);
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

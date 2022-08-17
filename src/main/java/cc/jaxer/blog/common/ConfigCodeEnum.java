package cc.jaxer.blog.common;

public enum ConfigCodeEnum
{
    access("登陆密码"),
    unsplash_appid("unsplash开发者id"),
    unsplash_proxy("unsplash代理地址"),
    blog_title("博客标题"),
    blog_desc("博客副标题，描述"),
    blog_logo_url("logo的url地址"),
    blog_keywords("meta关键字"),
    blog_description("meta的描述"),
    hl_num("头部链接。html格式（链接使用<a href=''></a>添加），可添加多个，从1开始 hl_1, hl_2 .. 类推"),
    footer_num("底部链接。html格式，可添加多个，从1开始 footer_1, footer_2 .. 类推"),
    bgm_url("首页bgm的url地址，请以http开头"),
    blog_domain("博客地址，请以http://开头，结尾不要/"),
    ;

    String desc;
    ConfigCodeEnum(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return this.desc;
    }

}

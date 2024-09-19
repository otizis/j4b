package cc.jaxer.blog.services;

import cc.jaxer.blog.common.ConfigCodeEnum;
import cc.jaxer.blog.entities.BlogInfoEntity;
import cc.jaxer.blog.entities.ConfigEntity;
import cc.jaxer.blog.entities.LinkEntity;
import cc.jaxer.blog.mapper.ConfigMapper;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigService
{
    @Autowired
    private ConfigMapper configMapper;

    @Value("${j4b.reply.open:false}")
    private boolean replyOpen;

    private static BlogInfoEntity blogInfoEntity = null;

    TimedCache<String, ConfigEntity> cachedConfig = CacheUtil.newTimedCache(60 * 60 * 1000);


    public BlogInfoEntity getBlogInfo()
    {
        if (blogInfoEntity != null)
        {
            return blogInfoEntity;
        }
        return loadBlogInfo();
    }

    private BlogInfoEntity loadBlogInfo()
    {
        QueryWrapper<ConfigEntity> queryWrapper = new QueryWrapper<ConfigEntity>().likeRight("code", "hl_")
                                                                                  .or()
                                                                                  .likeRight("code", "footer_")
                                                                                  .or()
                                                                                  .likeRight("code", "blog_")
                                                                                  .orderByAsc("code");
        List<ConfigEntity> configEntities = configMapper.selectList(queryWrapper);


        blogInfoEntity = new BlogInfoEntity();
        blogInfoEntity.setReplyOpen(replyOpen);
        // 链接列表
        List<LinkEntity> linkList = blogInfoEntity.getLinkList();
        // 底部列表
        List<String> footerList = blogInfoEntity.getFooterList();

        for (ConfigEntity configEntity : configEntities)
        {
            String code = configEntity.getCode();
            String v = configEntity.getV();
            if (code.startsWith("hl_"))
            {
                LinkEntity link = new LinkEntity();
                link.setName(code.replace("hl_", ""));
                link.setLink(v);
                linkList.add(link);
            }
            else if (code.startsWith("footer_"))
            {
                footerList.add(v);
            }
            else if (ConfigCodeEnum.blog_title.toString().equals(code))
            {
                blogInfoEntity.setTitle(v);
            }
            else if (ConfigCodeEnum.blog_desc.toString().equals(code))
            {
                blogInfoEntity.setDesc(v);
            }
            else if (ConfigCodeEnum.blog_logo_url.toString().equals(code))
            {
                blogInfoEntity.setLogoUrl(v);
            }
            else if (ConfigCodeEnum.blog_description.toString().equals(code))
            {
                blogInfoEntity.setLogoUrl(v);
            }
            else if (ConfigCodeEnum.blog_keywords.toString().equals(code))
            {
                blogInfoEntity.setLogoUrl(v);
            }
        }
        return blogInfoEntity;
    }

    public void reloadBlogInfo()
    {
        blogInfoEntity = null;
    }

    public String getConfDefaultCache(ConfigCodeEnum key,String defaultStr) {
        ConfigEntity cached = cachedConfig.get(key.toString());
        if(cached == null){
            ConfigEntity configEntity = configMapper.selectById(key.toString());
            if(configEntity == null){
                return defaultStr;
            }
            cachedConfig.put(key.toString(), configEntity);
            return configEntity.getV();
        }
        return cached.getV();
    }
    public String getConfDefault(ConfigCodeEnum key,String defaultStr) {
        ConfigEntity configEntity = configMapper.selectById(key.toString());
        return configEntity == null ? defaultStr :configEntity.getV();
    }
    public String getConf(ConfigCodeEnum key) {
        return getConfDefault(key, "");
    }
}

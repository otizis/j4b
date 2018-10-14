package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.TagEntity;
import cc.jaxer.blog.mapper.TagMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class TagController
{
    @Autowired
    private TagMapper tagMapper;

    @RequestMapping("/tag/list")
    public R list()
    {
        List<TagEntity> tagList = tagMapper.selectList(new QueryWrapper<TagEntity>());

        return R.ok("tagList", tagList);
    }


    @RequestMapping("/tag/save")
    @NeedLogin
    public R addOrUpdate(@RequestBody TagEntity tag)
    {
        if(StringUtils.isEmpty(tag.getTag())
                || tag.getTag().length()>20){
            return R.error();
        }
        if(StringUtils.isNotBlank(tag.getId())){
            tagMapper.updateById(tag);
            return R.ok();
        }

        tag.setId(UUID.randomUUID().toString().replace("-", ""));

        tagMapper.insert(tag);

        return R.ok("tag",tag);
    }



    @RequestMapping("/tag/del")
    @NeedLogin
    public R del(@RequestBody TagEntity tag)
    {
        tagMapper.deleteById(tag.getId());
        return R.ok();
    }
}

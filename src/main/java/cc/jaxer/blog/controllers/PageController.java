package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.PageEntity;
import cc.jaxer.blog.mapper.PageMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class PageController
{
    @Autowired
    private PageMapper pageMapper;

    @RequestMapping("/page/list")
    public R list(@RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
                  @RequestParam(name= "limit", defaultValue = "5", required = false) Integer limit)
    {
        IPage<PageEntity> pageEntityIPage = pageMapper.selectPage(new Page<>(page, limit), new
                QueryWrapper<PageEntity>()
                .eq("status",1)
                .excludeColumns(PageEntity.class, "content")
                .orderByDesc("create_at"));

        return R.ok("page", pageEntityIPage);
    }


    @RequestMapping("/page/save")
    @NeedLogin
    public R add(@RequestBody PageEntity page)
    {
        Date now = new Date();
        page.setCreateAt(now);
        page.setUpdateAt(now);
        page.setId(UUID.randomUUID().toString().replace("-", ""));
        String content = page.getContent();
        int len = 30;
        if(content.length() < len){
            page.setDesc(content);
        }else{
            page.setDesc(content.substring(0, len));
        }
        pageMapper.insert(page);

        return R.ok();
    }


    @RequestMapping("/page/edit")
    @NeedLogin
    public R list(@RequestBody PageEntity page)
    {
        page.setUpdateAt(new Date());
        pageMapper.updateById(page);
        return R.ok();
    }

    @RequestMapping("/page/info")
    @NeedLogin
    public R info(@RequestBody PageEntity page)
    {
        PageEntity result = pageMapper.selectById(page.getId());
        return R.ok("page",result);
    }

    @RequestMapping("/page/del")
    @NeedLogin
    public R del(@RequestBody PageEntity page)
    {
        page.setStatus(0);
        page.setUpdateAt(new Date());
        pageMapper.updateById(page);
        return R.ok();
    }
}

package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.PageEntity;
import cc.jaxer.blog.mapper.PageMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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


    @RequestMapping("/page/save")
    @NeedLogin
    public R add(@RequestBody PageEntity page)
    {
        Date now = new Date();
        page.setCreateAt(now);
        page.setUpdateAt(now);
        page.setId(UUID.randomUUID().toString().replace("-",""));
        pageMapper.insert(page);

        return R.ok();
    }

    @RequestMapping("/page/list")
    public R list(@RequestParam(name = "page",defaultValue = "1",required = false) Integer page,
                  @RequestParam(name = "limit",defaultValue = "5",required = false) Integer limit)
    {
        IPage<PageEntity> pageEntityIPage = pageMapper.selectPage(new Page<>(page, limit), new
                QueryWrapper<PageEntity>().orderByDesc("create_at"));

        return R.ok("page",pageEntityIPage);
    }

    @RequestMapping("/page/edit")
    @NeedLogin
    public R list(@RequestBody PageEntity page)
    {
        page.setUpdateAt(new Date());
        pageMapper.updateById(page);
        return R.ok();
    }
}

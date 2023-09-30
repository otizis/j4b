package cc.jaxer.blog.controllers;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import cc.jaxer.blog.services.TextDetectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.ReplyEntity;
import cc.jaxer.blog.mapper.ReplyMapper;

@RestController
public class ReplyController
{
    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private TextDetectService textDetectService;

    @RequestMapping("/reply/list")
    public R list(
        @RequestParam(name = "pageId", required = false) String pageId,
        @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
        @RequestParam(name= "limit", defaultValue = "5", required = false) Integer limit)
    {
        IPage<ReplyEntity> replyPage = replyMapper.selectPage(new Page<>(page, limit), new
                QueryWrapper<ReplyEntity>()
                .eq(StringUtils.isNotBlank(pageId),"page_id",pageId)
                .eq("status",1)
                .orderByDesc("create_at"));

        return R.ok("page", replyPage);
    }


    @RequestMapping("/reply/save")
    public R add(@RequestBody ReplyEntity reply,HttpServletRequest request)
    {
        if(StringUtils.isBlank(reply.getContent()))
        {
            return R.error();
        }
        if(reply.getContent().length() > 1000){
            return R.error(500,"留言过长，超过1000");
        }
        if(textDetectService.isBad(reply.getContent())){
            return R.error(500,"包含敏感词");
        }
        String realIp = request.getHeader("X-Real-IP");
        if(StringUtils.isBlank(realIp)){
            realIp = request.getRemoteAddr();
        }
        reply.setIp(realIp);
        reply.setCreateAt(new Date());
        reply.setId(UUID.randomUUID().toString().replace("-", ""));
        replyMapper.insert(reply);

        return R.ok();
    }

    @RequestMapping("/reply/check")
    public R check(@RequestBody ReplyEntity reply)
    {
        if(StringUtils.isBlank(reply.getContent()))
        {
            return R.error();
        }
        if(textDetectService.isBad(reply.getContent())){
            return R.error(500,"包含敏感词");
        }
        return R.ok();
    }


    @RequestMapping("/reply/update")
    @NeedLogin
    public R list(@RequestBody ReplyEntity reply)
    {
        replyMapper.updateById(reply);
        return R.ok();
    }



}

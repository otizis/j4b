package cc.jaxer.blog.controllers;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import cc.jaxer.blog.common.ConfigCodeEnum;
import cc.jaxer.blog.entities.TextDetectEntity;
import cc.jaxer.blog.services.ConfigService;
import cc.jaxer.blog.services.TextDetectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.ReplyEntity;
import cc.jaxer.blog.mapper.ReplyMapper;

@RestController
@Slf4j
public class ReplyController
{
    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private TextDetectService textDetectService;

    @Autowired
    private ConfigService configService;

    private String[] refererFilterConf = null;
    private long loadConfTime = 0;

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
    public R check(@RequestBody ReplyEntity reply,
                   @RequestHeader(value="Referer",required = false) String referer)
    {
        if(StringUtils.isEmpty(referer)){
            return R.error();
        }
        if(System.currentTimeMillis() > loadConfTime || reply.getStatus()!=null)
        {
            String conf = configService.getConfDefault(ConfigCodeEnum.referer_filter_conf, null);
            if(conf == null){
                refererFilterConf = null;
            }else{
                refererFilterConf = conf.split(";");
            }
            loadConfTime = System.currentTimeMillis() + 24*60*60*1000;
        }
        // 校验来源
        if(refererFilterConf !=null)
        {
            if(!StringUtils.containsAny(referer, refererFilterConf)){
                return R.error();
            }
        }
        if(StringUtils.isBlank(reply.getContent()))
        {
            return R.error();
        }
        if(textDetectService.isBad(reply.getContent())){
            // 输出日志，看看有哪些
            textDetectService.check(reply.getContent());
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


    @RequestMapping("/reply/addTextDetect")
    public R addTextDetect(@RequestBody TextDetectEntity textDetect)
    {
        if(StringUtils.isBlank(textDetect.getWord()))
        {
            return R.error();
        }
        if(textDetect.getWord().length() > 16){
            return R.error(500,"过长，超过16");
        }
        try{
            textDetect.setType(1);
            textDetectService.save(textDetect);
            textDetectService.reload();
            return R.ok();
        }catch (Exception e){
            return R.error(e.getMessage());
        }

    }

    @RequestMapping("/reply/delTextDetect")
    @NeedLogin
    public R delTextDetect(@RequestBody TextDetectEntity textDetect)
    {
        QueryWrapper<TextDetectEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("word", textDetect.getWord());
        textDetectService.remove(wrapper);
        textDetectService.reload();
        return R.ok();
    }

}

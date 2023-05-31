package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.AppConstant;
import cc.jaxer.blog.common.NeedAuth;
import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.ExtractEntity;
import cc.jaxer.blog.services.ExtractService;
import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/extract")
@Slf4j
public class ExtractController
{
    /**
     *  近64次添加，content不相同，避免短期内重复添加，同一个资源
     */
    private final static Cache<String,Integer> fifoCache = CacheUtil.newFIFOCache(64);


    @Value("${fileupload.path}")
    private String nginxServerPath;

    @Autowired
    private ExtractService extractService;

    @RequestMapping("/add")
    @NeedAuth
    public R add(@RequestBody ExtractEntity request){
        Assert.notNull(request.getTitle());
        Assert.notNull(request.getType());
        String content = request.getContent();
        Assert.notNull(content);
        Assert.notNull(request.getSourceUrl());
        if(request.getMemo()!=null){
            Assert.isTrue(request.getMemo().length() < 512,"memo过长");
        }

        request.setTitle(StrUtil.maxLength(request.getTitle(),250));
        request.setSourceUrl(StrUtil.maxLength(request.getSourceUrl(),510));

        if(request.getType() == 1||request.getType() == 5)
        {
            if(fifoCache.containsKey(content)){
                return R.error("已经存在相同内容");
            }
            fifoCache.put(content, AppConstant.PAGE_STATE_NORMAL);

            LocalDate now = LocalDate.now();
            String day = DateTimeFormatter.ofPattern("yyyyMMdd").format(now);
            String filename = UUID.randomUUID().toString();
            String path = URLUtil.getPath(content);
            String suffix = FileNameUtil.getSuffix(path);
            if(suffix.contains("/")){
                suffix = suffix.replace("/", "");
            }
            String filePath = File.separator + day + File.separator + filename + "." + suffix;

            final HttpResponse response = HttpRequest.get(content)
                                                     .header(Header.REFERER,request.getSourceUrl())
                                                     .timeout(3000).executeAsync();
            response.writeBody(new File(nginxServerPath + filePath), null);

            if(response.getStatus() == 200){
                request.setContent(AppConstant.OSS_PATH +filePath.replace(File.separator,"/"));
            }
        }
        else if(request.getType() == 4)
        {
            BufferedImage bufferedImage = ImgUtil.toImage(content.split(",")[1]);
            LocalDate now = LocalDate.now();
            String day = DateTimeFormatter.ofPattern("yyyyMMdd").format(now);
            String filename = UUID.randomUUID().toString();
            String filePath = File.separator + day + File.separator + filename + ".png";
            ImgUtil.writePng(bufferedImage, FileUtil.getOutputStream(nginxServerPath + filePath) );
            request.setContent(AppConstant.OSS_PATH +filePath.replace(File.separator,"/"));
        }
        Date now = new Date();
        request.setCreateAt(now);
        request.setUpdateAt(now);
        extractService.save(request);
        return R.ok();
    }

    @RequestMapping("/updateStatus")
    @NeedLogin
    public R updateStatus(@RequestBody ExtractEntity request){
        Assert.notNull(request.getStatus());
        extractService.updateById(request);
        return R.ok();
    }


    /**
     * 删除所有已删除的记录，同时删除文件
     * @return
     */
    @RequestMapping("/deleteAllZero")
    @NeedLogin
    public R deleteAllZero()
    {
        List<ExtractEntity> list = extractService.list(new QueryWrapper<ExtractEntity>().eq(ExtractEntity.STATUS, 0));
        for (ExtractEntity extractEntity : list)
        {
            Integer type = extractEntity.getType();
            if(type == 1||type == 4||type == 5)
            {
                String content = extractEntity.getContent();
                FileUtil.del(nginxServerPath + content);
            }
        }
        extractService.remove(new QueryWrapper<ExtractEntity>().eq(ExtractEntity.STATUS,0));
        return R.ok();
    }
}

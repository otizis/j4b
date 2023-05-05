package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.AppConstant;
import cc.jaxer.blog.common.NeedAuth;
import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.ExtractEntity;
import cc.jaxer.blog.services.ExtractService;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
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
import java.util.UUID;

@RestController
@RequestMapping("/extract")
@Slf4j
public class ExtractController
{
    @Value("${fileupload.path}")
    private String nginxServerPath;

    @Autowired
    private ExtractService extractService;

    @RequestMapping("/add")
    @NeedAuth
    public R add(@RequestBody ExtractEntity request){
        Assert.notNull(request.getTitle());
        Assert.notNull(request.getType());
        Assert.notNull(request.getContent());
        Assert.notNull(request.getSourceUrl());
        if(request.getMemo()!=null){
            Assert.isTrue(request.getMemo().length() < 512,"memo过长");
        }

        request.setTitle(StrUtil.maxLength(request.getTitle(),250));
        request.setSourceUrl(StrUtil.maxLength(request.getSourceUrl(),510));

        if(request.getType() == 1)
        {
            // 已经添加过的图片地址，不添加了
            ExtractEntity queryByContent = new ExtractEntity();
            queryByContent.setContent(request.getContent());
            int count = extractService.count(new QueryWrapper<>(queryByContent));
            if(count != 0){
                return R.error("已经存在相同内容");
            }

            LocalDate now = LocalDate.now();
            String day = DateTimeFormatter.ofPattern("yyyyMMdd").format(now);
            String filename = UUID.randomUUID().toString();
            String suffix = FileNameUtil.getSuffix(request.getContent());
            String filePath = File.separator + day + File.separator + filename + "." + suffix;

            final HttpResponse response = HttpRequest.get(request.getContent())
                                                     .header(Header.REFERER,request.getSourceUrl())
                                                     .timeout(3000).executeAsync();
            response.writeBody(new File(nginxServerPath + filePath), null);

            if(response.getStatus() == 200){
                request.setContent(AppConstant.OSS_PATH +filePath.replace(File.separator,"/"));
            }
        }
        else if(request.getType() == 4)
        {
            BufferedImage bufferedImage = ImgUtil.toImage(request.getContent().split(",")[1]);
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
}

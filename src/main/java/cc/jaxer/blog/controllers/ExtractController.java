package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.*;
import cc.jaxer.blog.entities.ExtractEntity;
import cc.jaxer.blog.services.ConfigService;
import cc.jaxer.blog.services.ExtractService;
import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/extract")
@Slf4j
public class ExtractController
{
    /**
     *  近64次添加，content不相同，避免短期内重复添加，同一个资源
     */
    private final static Cache<String,Integer> fifoCache = CacheUtil.newFIFOCache(64);

    @Autowired
    private ConfigService configService;


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
            String videoUrl = content;
            if(isBilibili(request)){
                String sourceUrl = request.getSourceUrl();
                // https://www.bilibili.com/video/BV1ax4y1k7a3/?
                Pattern compile = Pattern.compile("/(BV[^/]+?)/");
                Matcher matcher = compile.matcher(sourceUrl);
                matcher.find();
                String bvid = matcher.group(1);
                String cookie = configService.getConfDefault(ConfigCodeEnum.bilibili_cookie,null);
                if(cookie == null){
                    videoUrl = downloadBiliVideo(bvid);
                }else{
                    videoUrl = downloadBiliWithCookie(bvid,cookie);
                }
                if(videoUrl == null){
                    return R.error();
                }
            }
            LocalDate now = LocalDate.now();
            String day = DateTimeFormatter.ofPattern("yyyyMM").format(now);
            String filename = UUID.randomUUID().toString();
            String path = URLUtil.getPath(videoUrl);
            String suffix = FileNameUtil.getSuffix(path);
            if(suffix.contains("/")){
                suffix = suffix.replace("/", "");
            }
            if(suffix.length() > 5){
                suffix = "";
            }
            String filePath = File.separator + day + File.separator + filename + "." + suffix;
            String pathname = nginxServerPath + filePath;
            FileUtil.mkParentDirs(pathname);
            final HttpResponse response = HttpRequest.get(videoUrl)
                                                     .header(Header.REFERER,request.getSourceUrl())
                                                     .timeout(3000).executeAsync();
            response.writeBody(new File(pathname), null);

            if(response.getStatus() == 200){
                request.setContent(AppConstant.OSS_PATH +filePath.replace(File.separator,"/"));
            }
        }
        else if(request.getType() == 4)
        {
            BufferedImage bufferedImage = ImgUtil.toImage(content.split(",")[1]);
            LocalDate now = LocalDate.now();
            String day = DateTimeFormatter.ofPattern("yyyyMM").format(now);
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
                content = content.replaceFirst(AppConstant.OSS_PATH,"");
                FileUtil.del(nginxServerPath + content);
            }
        }
        extractService.remove(new QueryWrapper<ExtractEntity>().eq(ExtractEntity.STATUS,0));
        return R.ok();
    }


    private boolean isBilibili(ExtractEntity request)
    {
        if(request.getType() == 5 && request.getSourceUrl().contains("bilibili.com"))
        {
            String sourceUrl = request.getSourceUrl();
            // https://www.bilibili.com/video/BV1ax4y1k7a3/?
            Pattern compile = Pattern.compile("/(BV[^/]+?)/");
            Matcher matcher = compile.matcher(sourceUrl);
            return matcher.find();
        }
        return false;
    }

    /**
     * 匿名h5获取
     * @param bvid
     */
    private String downloadBiliVideo(String bvid )
    {
        final HttpResponse response = HttpRequest.get("https://m.bilibili.com/video/"+bvid+"/")
                                                 .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                                                 .header("Accept-Encoding","gzip, deflate, br, zstd")
                                                 .header("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
                                                 .timeout(3000)
                                                 .executeAsync();

        String body = response.body();

        Pattern compile = Pattern.compile("\"(https://[^\"]+\\.mp4[^\"]+?)\"");
        Matcher matcher = compile.matcher(body);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return null;
    }

    private String downloadBiliWithCookie(String bvid ,String cookie)
    {
        String url = "https://www.bilibili.com/video/" + bvid + "/";
        String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;" +
                "q=0.8,application/signed-exchange;v=b3;q=0.7";
        String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) " +
                "Version/16.0 Safari/605.1.15";

        // 登陆后获取高清视频地址
        final HttpResponse response = HttpRequest
                .get(url)
                .header("Accept", accept)
                .header("Accept-Encoding",
                        "gzip, deflate, br, zstd")
                .header("User-Agent", ua)
                .header("Cookie", cookie)
                .timeout(3000)
                .executeAsync();
        String body = response.body();
        response.close();
        Pattern compile = Pattern.compile("<script>window.__playinfo__=(.+?)</script>");
        Matcher matcher = compile.matcher(body);
        if (!matcher.find())
        {
            return null;
        }
        String palyInfo = matcher.group(1);

        // 视频流
        JSONObject jsonObject = JSONUtil.parseObj(palyInfo);
        String videoUrl = jsonObject
                .getJSONObject("data")
                .getJSONObject("dash")
                .getJSONArray("video")
                .getJSONObject(0)
                .getStr("base_url");
        LocalDate now = LocalDate.now();
        String day = DateTimeFormatter.ofPattern("yyyyMM").format(now);
        String filePath = File.separator + day + File.separator;

        final HttpResponse downloadResp = HttpRequest
                .get(videoUrl)
                .header("Accept", accept)
                .header("Accept-Encoding",
                        "gzip, deflate, br, zstd")
                .header("User-Agent", ua)
                .header("Cookie", cookie)
                .header(Header.REFERER, url)
                .timeout(3000)
                .executeAsync();
        downloadResp.writeBody(new File(nginxServerPath + filePath + bvid + ".m4s"), null);
        downloadResp.close();
        // 音频流
        String audioUrl = jsonObject
                .getJSONObject("data")
                .getJSONObject("dash")
                .getJSONArray("audio")
                .getJSONObject(0)
                .getStr("base_url");
        final HttpResponse downloadResp2 = HttpRequest
                .get(audioUrl)
                .header("Accept", accept)
                .header("Accept-Encoding",
                        "gzip, deflate, br, zstd")
                .header("User-Agent", ua)
                .header("Cookie", cookie)
                .header(Header.REFERER, url)
                .timeout(3000)
                .executeAsync();
        downloadResp2.writeBody(new File(nginxServerPath + filePath + bvid + ".mp3"), null);
        downloadResp2.close();

        return meg(nginxServerPath + filePath, bvid);
    }

    /**
     * 调用ffmpeg合并
     * @param path
     * @param bvid
     * @return
     */
    private String meg(String path,String bvid)
    {
        Runtime runtime = Runtime.getRuntime();  //获取Runtime实例
        //执行命令
        try {
            String[] command = {"cmd", "/c", "ffmpeg -i "+
                    path+bvid+".m4s"+
                    " -i " +
                    path+bvid+".mp3" +
                    " -codec copy " +
                    path+bvid+".mp4"};
            Process process = runtime.exec(command);

            int proc = process.waitFor();
            if (proc == 0) {
                log.info("执行成功,{}",bvid);
            } else {
                log.info("执行失败:{},{}" , proc,bvid);
            }
        } catch (IOException | InterruptedException e) {
            log.error("",e);
        }
        return path + bvid + ".mp4";
    }

    /**
     *
     * @param bvidList
     * @param cookie
     */
    private void downloadBiliWithCookieBatch(String[] bvidList,String cookie)
    {
        for (int i = 0; i < bvidList.length; i++)
        {
            String bvid = bvidList[i];
            downloadBiliWithCookie(bvid,cookie);
            ThreadUtil.safeSleep(5_000);
        }
    }
}

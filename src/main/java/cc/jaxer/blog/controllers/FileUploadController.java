package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
@Slf4j
public class FileUploadController
{
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${fileupload.path}")
    private String nginxServerPath;

    @RequestMapping("/upload")
    @ResponseBody
    @NeedLogin
    public R upload(@RequestParam("file") MultipartFile file)
    {
        LocalDate now = LocalDate.now();
        String day = DateTimeFormatter.ofPattern("yyyyMMdd").format(now);
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString();
        if (StringUtils.isNotBlank(extension))
        {
            filename += extension;
        }
        try
        {
            if (!nginxServerPath.endsWith(File.separator))
            {
                nginxServerPath += File.separator;
            }

            String path = nginxServerPath + day + File.separator;
            File dayDir = new File(path);
            logger.info(dayDir.getAbsolutePath());
            if (!dayDir.exists())
            {
                boolean create = createDirWithParent(dayDir);
                if (!create)
                {
                    return R.error(500, path + "不能创建");
                }
            }
            File dest = new File(path + filename);
            logger.info(dest.getAbsolutePath());
            file.transferTo(dest);
        }
        catch (IOException e)
        {
            logger.error("",e);
            return R.error();
        }
        return R.ok("location", "/oss/" + day + "/" + filename);
    }

    @RequestMapping(path = {"/fileList.html"})
    @NeedLogin(isPage = true)
    public String fileList(@RequestParam(value = "currPath",required = false) String currPath,
                           @RequestParam(value = "path",required = false) String path,
                           ModelMap modelMap )
    {
        String gotoPath = StringUtils.defaultString(currPath) ;
        if(StringUtils.isNoneBlank(path) && !StringUtils.contains(path,"..")){
            gotoPath += "/" + path;
        }

        File file = new File(nginxServerPath + "/" + gotoPath);
        if(StringUtils.contains(path,"..")){
            File rootFile = new File(nginxServerPath);

            if(FileUtil.isSub(rootFile,file.getParentFile())){
                file = file.getParentFile();
                gotoPath = gotoPath.substring(0,gotoPath.lastIndexOf("/"));
            }
        }
        if(file.exists() && file.isDirectory()){
            File[] list = file.listFiles();
            modelMap.addAttribute("fileList", list);
            modelMap.addAttribute("currPath", gotoPath);

        }else{
            return "redirect:/fileList.html";
        }

        return "admin/fileList";
    }

    @RequestMapping(path = {"/uploadOrig"})
    @NeedLogin
    public String uploadOrig(@RequestParam("path") String path,
                             @RequestParam("file") MultipartFile file)
    {
        if(StringUtils.contains(path,"..")){
            return "redirect:/fileList.html";
        }
        try
        {
            File dayDir = new File( nginxServerPath + path);
            logger.info(dayDir.getAbsolutePath());
            if (!dayDir.exists())
            {
                boolean create = createDirWithParent(dayDir);
                if (!create)
                {
                    return "redirect:/fileList.html?currPath="+path;
                }
            }
            File dest = new File(dayDir.getAbsolutePath() + File.separator + file.getOriginalFilename());
            logger.info(dest.getAbsolutePath());
            file.transferTo(dest);
        }
        catch (IOException e)
        {
            logger.error("",e);
        }
        return "redirect:/fileList.html?currPath="+path;
    }

    @RequestMapping(path = {"/downloadByUrl"})
    @NeedLogin
    public String downloadByUrl(@RequestParam("url") String url)
    {
        LocalDate now = LocalDate.now();
        String day = DateTimeFormatter.ofPattern("yyyyMMdd").format(now);
        String path = nginxServerPath + day + File.separator;
        File dayDir = new File( path);
        if (!dayDir.exists())
        {
            boolean create = createDirWithParent(dayDir);
            if (!create)
            {
                return "redirect:/fileList.html";
            }
        }
        long length = HttpUtil.downloadFile(url, path);
        return "redirect:/fileList.html";
    }

    @RequestMapping(path = {"/createDir"})
    @NeedLogin
    public String createDir(@RequestParam(value = "currPath",required = false) String currPath,
                            @RequestParam("dirName") String dirName)
    {
        dirName = FileNameUtil.cleanInvalid(dirName);

        File file = new File(nginxServerPath +File.separator+ currPath + File.separator + dirName);
        file.mkdir();
        return "redirect:/fileList.html?currPath"+currPath;
    }
    @RequestMapping(path = {"/delFile"})
    @NeedLogin
    public String uploadOrig(@RequestParam("path") String path)
    {
        if(StringUtils.contains(path,"..")){
            return "redirect:/fileList.html";
        }
        File file = new File(nginxServerPath + path);
        if(file.exists() ){
            file.delete();
        }
        return "redirect:/fileList.html";
    }


    private boolean createDirWithParent(File dir)
    {
        log.debug(dir.getAbsolutePath());
        File parentFile = dir.getParentFile();
        String parentFileAbsolutePath = parentFile.getAbsolutePath();
        parentFile = new File(parentFileAbsolutePath);
        if (parentFile.exists())
        {
            return dir.mkdir();
        }
        return createDirWithParent(parentFile);
    }

}

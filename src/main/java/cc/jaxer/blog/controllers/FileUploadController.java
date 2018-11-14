package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
public class FileUploadController
{
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${fileupload.path}")
    private String nginxServerPath;

    @RequestMapping("/upload")
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

    private boolean createDirWithParent(File dir)
    {
        System.out.println(dir.getAbsolutePath());
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

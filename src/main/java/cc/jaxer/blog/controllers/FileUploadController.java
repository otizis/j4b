package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.common.R;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileUploadController
{
    @Value("${fileupload.path}")
    private String nginxServerPath;

    @RequestMapping("/upload")
    @NeedLogin
    public R upload(@RequestParam("file") MultipartFile file)
    {
        String day = DateFormatUtils.format(new Date(), "YYYYMMDD");
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString();
        if (StringUtils.isNotBlank(extension))
        {
            filename += ("." + extension);
        }
        try
        {
            String path = nginxServerPath + File.separator + day + File.separator;
            File dayDir = new File(path);
            if (!dayDir.exists())
            {
                boolean create = dayDir.mkdir();
                if (!create)
                {
                    return R.error(500, path + "不能创建");
                }
            }
            file.transferTo(new File(path + filename));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return R.error();
        }
        return R.ok("location", "/oss/" + day + "/" + filename);
    }

}

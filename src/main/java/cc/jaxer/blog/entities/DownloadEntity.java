package cc.jaxer.blog.entities;

import lombok.Data;

import java.util.Date;

@Data
public class DownloadEntity
{
    String url;// VARCHAR(256),
    String savePath;
    Date createAt;// TIMESTAMP,
    Date updateAt;// TIMESTAMP,
    long progressSize;
    /**
     * 1 初始化 2 进行中 3 已完成 4 错误
     */
    Integer status;// TINYINT DEFAULT 1
}

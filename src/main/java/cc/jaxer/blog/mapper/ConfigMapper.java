package cc.jaxer.blog.mapper;

import cc.jaxer.blog.entities.ConfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ConfigMapper extends BaseMapper<ConfigEntity>
{
    /**
     * h2 备份命令
     * @param backupFile
     */
    @Select("BACKUP TO #{filename}")
    void backup(@Param("filename") String backupFile);
}

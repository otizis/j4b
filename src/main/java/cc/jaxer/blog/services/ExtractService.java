package cc.jaxer.blog.services;

import cc.jaxer.blog.entities.ExtractEntity;
import cc.jaxer.blog.mapper.ExtractMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ExtractService extends ServiceImpl<ExtractMapper,ExtractEntity> implements IService<ExtractEntity>
{

}

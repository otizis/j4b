package cc.jaxer.blog.services;

import cc.jaxer.blog.common.AppConstant;
import cc.jaxer.blog.common.J4bUtils;
import cc.jaxer.blog.entities.PageEntity;
import cc.jaxer.blog.entities.PageTagEntity;
import cc.jaxer.blog.entities.TagEntity;
import cc.jaxer.blog.mapper.PageMapper;
import cc.jaxer.blog.mapper.PageTagMapper;
import cc.jaxer.blog.mapper.TagMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PageService  extends ServiceImpl<PageMapper,PageEntity>  implements IService<PageEntity>{

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private PageTagMapper pageTagMapper;

    public IPage<PageEntity> getPageListByTag(String tagId,IPage<PageEntity> page){
        boolean login = J4bUtils.isLogin();

        QueryWrapper<PageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(!login, "status", AppConstant.PAGE_STATE_NORMAL)
                .ne(login,"status",AppConstant.PAGE_STATE_DEL)
                .exists(" select 1 from T_PAGE_TAG where T_PAGE.ID = T_PAGE_TAG.PAGE_ID AND T_PAGE_TAG.TAG_ID = '" + tagId + "'")
                .orderByDesc("create_at");

        TagEntity tagEntity = tagMapper.selectById(tagId);
        ArrayList<TagEntity> tagList = new ArrayList<>();
        tagList.add(tagEntity);
        IPage<PageEntity> pageEntityIPage = pageMapper.selectPage(page, queryWrapper);
        pageEntityIPage.getRecords().forEach((item)-> item.setTagList(tagList));
        return pageEntityIPage;

    }

    public List<TagEntity> getTagListByPageId(String pageId){

        List<PageTagEntity> pageTagEntities = pageTagMapper.selectList(new QueryWrapper<PageTagEntity>().eq("page_id", pageId));
        Set<String> tagIdSet = pageTagEntities.stream().map(PageTagEntity::getTagId).collect(Collectors.toSet());
        if(tagIdSet.isEmpty()){
            return null;
        }
        return tagMapper.selectList(new QueryWrapper<TagEntity>().in("id", tagIdSet));
    }
}

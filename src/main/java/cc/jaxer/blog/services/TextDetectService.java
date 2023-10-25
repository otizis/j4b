package cc.jaxer.blog.services;

import cc.jaxer.blog.entities.TextDetectEntity;
import cc.jaxer.blog.mapper.TextDetectMapper;
import cn.hutool.dfa.WordTree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 文本检测服务
 */
@Service
@Slf4j
public class TextDetectService  extends ServiceImpl<TextDetectMapper, TextDetectEntity> implements IService<TextDetectEntity>
{
    private static final WordTree tree = new WordTree();


    public void reload() {
        tree.clear();



        int page = 1;
        int pageSize = 3000;
        IPage<TextDetectEntity> pageResult = this.page(new Page<>(page,pageSize));

        if(pageResult.getTotal() == 0)
        {
            List<TextDetectEntity> list = new ArrayList<>();
            try {
                InputStream inputStream = new ClassPathResource("uniq_sensitive_words.txt").getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    TextDetectEntity textDetectEntity = new TextDetectEntity();
                    textDetectEntity.setWord(line);
                    textDetectEntity.setType(0);
                    list.add(textDetectEntity);
                    if(list.size() > 1000){
                        this.saveBatch(list);
                        list.clear();
                    }
                }
                if(list.size() > 0){
                    this.saveBatch(list);
                    list.clear();
                }
                inputStream.close();
            } catch (IOException e) {
                log.error("",e);
            }
        }

        long pages = pageResult.getPages();
        while (page < pages){
            pageResult = this.page(new Page<>(page,pageSize));
            pageResult.getRecords().forEach(x->{
                tree.addWord(x.getWord());
            });
            page ++;
        }
    }

    public boolean isBad(String temp) {
        if(tree.isEmpty()){
            reload();
        }
        return tree.isMatch(temp);
    }

    public void check(String temp) {
        String match = tree.match(temp);
        log.error("{},-bad for->,{}",temp,match);
    }

}

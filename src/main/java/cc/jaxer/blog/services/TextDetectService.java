package cc.jaxer.blog.services;

import cn.hutool.dfa.WordTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 文本检测服务
 */
@Service
@Slf4j
public class TextDetectService
{
    private static final WordTree tree = new WordTree();
    static{
        reload();
    }
    public static void reload(){
        tree.clear();

        try {
            InputStream inputStream = new ClassPathResource("sensitive_words_lines.txt").getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                tree.addWord(line);
            }
            inputStream.close();
        } catch (IOException e) {
            log.error("",e);
        }

    }

    public boolean isBad(String temp) {
        return tree.isMatch(temp);
    }

    public static void check(String temp) {
        String match = tree.match(temp);
        System.out.println(match);
    }

    public static void main(String[] args) {
        TextDetectService.check("今天是↵9月5日↵星期二");
    }
}
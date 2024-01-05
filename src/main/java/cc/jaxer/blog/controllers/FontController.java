package cc.jaxer.blog.controllers;

import cn.hutool.core.util.StrUtil;
import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.tools.sfnttool.GlyphCoverage;
import com.google.typography.font.tools.subsetter.RenumberingSubsetter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
public class FontController {

    @Value("${j4b.font.path}")
    private String fontPath;

    @RequestMapping(path = {"/font/{fontName}/mini.ttf"})
    public void test(HttpServletResponse response,
                     @PathVariable("fontName") String fontName,
                     @RequestParam("queryString") String queryString)
            throws IOException
    {
        if (StrUtil.isBlank(queryString))
        {
            queryString = "动字";
        }
        queryString = queryString.replaceAll("\\n", "");
        System.out.println(queryString);
        String pathname = null;
        //  {loaded:false, name:"钉钉进步体",url:"https://dadan.jaxer.cc/oss/fonts/DingTalkJinBuTi.ttf",nameImg:"/images/fonts/dingdingjingbu.png"},
        // {loaded:false, name:"三极泼墨",url:"https://dadan.jaxer.cc/oss/fonts/sjpm.ttf",nameImg:"/images/fonts/sjpm.png"},
        // {loaded:false, name:"千图小兔体",url:"https://dadan.jaxer.cc/oss/fonts/QianTuXiaoTuTi-2.ttf",nameImg:"/images/fonts/qiantuxiaotu.png"},
        // {loaded:false, name:"云峰静龙行书",url:"https://dadan.jaxer.cc/oss/fonts/yunfengxingshu.ttf",nameImg:"/images/fonts/yunfengxingshu.png"},
        // {loaded:false, name:"点点像素字体",url:"https://dadan.jaxer.cc/oss/fonts/ddxs.ttf",nameImg:"/images/fonts/ddxs.png"},
        // {loaded:false, name:"拼音",url:"https://dadan.jaxer.cc/oss/fonts/pinyin.ttf",nameImg:"/images/fonts/pinyin.png"},
        switch (fontName)
        {
            case "钉钉进步体":
                pathname = fontPath +  "DingTalkJinBuTi.ttf";
                break;
            case "三极泼墨":
                pathname = fontPath +  "sjpm.ttf";
                break;
            case "千图小兔体":
                pathname = fontPath +  "QianTuXiaoTuTi-2.ttf";
                break;
            case "云峰静龙行书":
                pathname = fontPath +  "yunfengxingshu.ttf";
                break;
            case "点点像素字体":
                pathname = fontPath +  "ddxs.ttf";
                break;
            case "拼音":
                pathname = fontPath +  "pinyin.ttf";
                break;
            case "寒蝉圆黑体":
                pathname = fontPath +  "ChillRoundGothic_Bold.ttf";
                break;
            default:
                pathname =  fontPath +  "DingTalkJinBuTi.ttf";
        }

        File sourceFont = new File(pathname);

        FontFactory var4 = FontFactory.getInstance();
        FileInputStream fontInput = null;

        try {
            fontInput = new FileInputStream(sourceFont);
            byte[] var6 = new byte[(int) sourceFont.length()];
            fontInput.read(var6);
            Font[] var7 = null;
            var7 = var4.loadFonts(var6);
            Font var8 = var7[0];
            ArrayList var9 = new ArrayList();
            var9.add(CMapTable.CMapId.WINDOWS_BMP);
            Object var10 = null;

            for (int var11 = 0; var11 < 1; ++var11) {
                Font var12 = var8;
                if (queryString != null) {
                    RenumberingSubsetter var13 = new RenumberingSubsetter(var8, var4);
                    var13.setCMaps(var9, 1);
                    List var14 = GlyphCoverage.getGlyphCoverage(var8, queryString);
                    var13.setGlyphs(var14);
                    HashSet var15 = new HashSet();
                    var15.add(Tag.GDEF);
                    var15.add(Tag.GPOS);
                    var15.add(Tag.GSUB);
                    var15.add(Tag.kern);
                    var15.add(Tag.hdmx);
                    var15.add(Tag.vmtx);
                    var15.add(Tag.VDMX);
                    var15.add(Tag.LTSH);
                    var15.add(Tag.DSIG);
                    var15.add(Tag.vhea);
                    var15.add(Tag.intValue(new byte[]{109, 111, 114, 116}));
                    var15.add(Tag.intValue(new byte[]{109, 111, 114, 120}));
                    var13.setRemoveTables(var15);
                    var12 = var13.subset().build();
                }

                var4.serializeFont(var12, response.getOutputStream());
            }
        } finally {
            if (fontInput != null) {
                fontInput.close();
            }
        }


    }
}

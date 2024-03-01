package cc.jaxer.blog.controllers;

import cn.hutool.core.util.StrUtil;
import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.tools.sfnttool.GlyphCoverage;
import com.google.typography.font.tools.subsetter.RenumberingSubsetter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("fontname:{},str::{}",fontName,queryString);
        String pathname = null;
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

    @RequestMapping(path = {"/font/{fontName}/mini2.ttf"})
    public void miniFont(HttpServletResponse response,
                     @PathVariable("fontName") String fontName,
                     @RequestParam("queryString") String queryString)
            throws IOException
    {
        if (StrUtil.isBlank(queryString))
        {
            queryString = "动字";
        }
        queryString = queryString.replaceAll("\\n", "");
        log.info("fontname:{},str::{}",fontName,queryString);
        String pathname = fontPath +  fontName;
        File sourceFont = new File(pathname);
        if(!sourceFont.exists()){
            sourceFont =  new File(fontPath +  "DingTalkJinBuTi.ttf");
        }

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

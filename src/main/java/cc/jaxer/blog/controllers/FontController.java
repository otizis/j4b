package cc.jaxer.blog.controllers;

import cn.hutool.core.io.IoUtil;
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

        FontFactory fontFactory = FontFactory.getInstance();

        try {
            byte[] fontBytes = IoUtil.readBytes(new FileInputStream(sourceFont));
            Font[] fonts = fontFactory.loadFonts(fontBytes);
            Font font = fonts[0];
            ArrayList var9 = new ArrayList();
            var9.add(CMapTable.CMapId.WINDOWS_BMP);

            RenumberingSubsetter renumberingSubsetter = new RenumberingSubsetter(font, fontFactory);
            renumberingSubsetter.setCMaps(var9, 1);
            List glyphCoverage = GlyphCoverage.getGlyphCoverage(font, queryString);
            renumberingSubsetter.setGlyphs(glyphCoverage);
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
            renumberingSubsetter.setRemoveTables(var15);
            Font resultFont = renumberingSubsetter.subset().build();

            fontFactory.serializeFont(resultFont, response.getOutputStream());
        } finally {

        }


    }

    /**
     * 字体列表
     * {
     *  loaded:false,
     *  miniId:"QianTuXiaoTuTi-2.ttf",
     *  name:"千图小兔体",
     *  url:"https://dadan.jaxer.cc/oss/fonts/QianTuXiaoTuTi-2.ttf",
     *  nameImg:"https://dadan.jaxer.cc/oss/createGif/fonts/qiantuxiaotu.png"
     * }
     */
}

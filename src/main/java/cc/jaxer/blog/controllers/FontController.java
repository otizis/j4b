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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@Slf4j
public class FontController
{

    @Value("${j4b.font.path}")
    private String fontPath;

    private static HashSet<Integer> removeTables = new HashSet<>();
    static {
        removeTables.add(Tag.GDEF);
        removeTables.add(Tag.GPOS);
        removeTables.add(Tag.GSUB);
        removeTables.add(Tag.kern);
        removeTables.add(Tag.hdmx);
        removeTables.add(Tag.vmtx);
        removeTables.add(Tag.VDMX);
        removeTables.add(Tag.LTSH);
        removeTables.add(Tag.DSIG);
        removeTables.add(Tag.vhea);
        removeTables.add(Tag.intValue(new byte[]{109, 111, 114, 116}));
        removeTables.add(Tag.intValue(new byte[]{109, 111, 114, 120}));
    }


    @RequestMapping(path = {"/font/{fontName}/mini2.ttf"})
    public void miniFont(HttpServletResponse response,
                         @PathVariable("fontName") String fontName,
                         @RequestParam("queryString") String queryString) throws IOException
    {
        if (StrUtil.isBlank(queryString))
        {
            queryString = "动字1234567890abcdefghijklnmopqrstuvwxyz";
        }
        queryString = queryString.replaceAll("\\n", "");
        log.info("fontname:{},str::{}", fontName, queryString);
        String pathname = fontPath + fontName;
        Path path = Paths.get(pathname);
        File sourceFont = path.toFile();
        if (!sourceFont.exists())
        {
            path = Paths.get(fontPath + "DingTalkJinBuTi.ttf");
        }

        FontFactory fontFactory = FontFactory.getInstance();

        byte[] fontBytes = Files.readAllBytes(path);
        Font[] fonts = fontFactory.loadFonts(fontBytes);
        Font font = fonts[0];

        ArrayList<CMapTable.CMapId> idList = new ArrayList<>();
        idList.add(CMapTable.CMapId.WINDOWS_BMP);

        RenumberingSubsetter renumberingSubsetter = new RenumberingSubsetter(font, fontFactory);
        renumberingSubsetter.setCMaps(idList, 1);

        List<Integer> glyphCoverage = GlyphCoverage.getGlyphCoverage(font, queryString);
        renumberingSubsetter.setGlyphs(glyphCoverage);
        renumberingSubsetter.setRemoveTables(removeTables);

        Font resultFont = renumberingSubsetter.subset().build();
        fontFactory.serializeFont(resultFont, response.getOutputStream());
        response.flushBuffer();
    }

}

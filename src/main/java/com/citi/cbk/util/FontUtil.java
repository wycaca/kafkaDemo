package com.citi.cbk.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.ClassUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fgm on 2017/4/22.
 */
@Slf4j
public class FontUtil {

    private static final Map<String, Font> fontCache = new ConcurrentHashMap<>();

    public static String getFontPath(String fontName) {
        //添加字体，以支持中文
        String classpath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String fontPath = classpath + "fonts/" + fontName;
        return fontPath;
    }

    /**
     * 加载自定义字体
     */
    public static Font loadFont(String fontFileName, int style, float fontSize) throws IOException {
        FileInputStream fis = null;
        String key = fontFileName + "|" + style;
        Font dynamicFont = fontCache.get(key);
        if (dynamicFont == null) {
            try {
                File file = new File(fontFileName);
                fis = new FileInputStream(file);
                dynamicFont = Font.createFont(style, fis);
                fontCache.put(key, dynamicFont);

            } catch (Exception e) {
                return new Font("宋体", Font.PLAIN, 14);
            } finally {
                fis.close();
            }
        }
        Font dynamicFontPt = dynamicFont.deriveFont(fontSize);
        return dynamicFontPt;
    }

    public static Font getFont(int style, float fontSize) {
        try {
            String fontPath = getFontPath("ping_fang_regular.ttf");
            Font font = loadFont(fontPath, style, fontSize);
            return font;
        } catch (IOException e) {
            log.error("字体加载异常{}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}

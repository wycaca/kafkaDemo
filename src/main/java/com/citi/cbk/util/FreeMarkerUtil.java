package com.citi.cbk.util;

import com.citi.cbk.exception.FreeMarkerException;
import com.citi.cbk.exception.PDFException;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fgm on 2017/4/22.
 * FREEMARKER 模板工具类
 */
@Slf4j
public class FreeMarkerUtil {

    private static final String WINDOWS_SPLIT = "\\";

    private static final String UTF_8 = "UTF-8";

    private static Map<String, FileTemplateLoader> fileTemplateLoaderCache = new ConcurrentHashMap<>();

    private static Map<String, Configuration> configurationCache = new ConcurrentHashMap<>();

    public static Configuration getConfiguration(String templateFilePath) {
        if (null != configurationCache.get(templateFilePath)) {
            return configurationCache.get(templateFilePath);
        }
        Configuration config = new Configuration(Configuration.VERSION_2_3_32);
        config.setDefaultEncoding(UTF_8);
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        FileTemplateLoader fileTemplateLoader = null;
        if (null != fileTemplateLoaderCache.get(templateFilePath)) {
            fileTemplateLoader = fileTemplateLoaderCache.get(templateFilePath);
        }
        try {
            fileTemplateLoader = new FileTemplateLoader(new File(templateFilePath));
            fileTemplateLoaderCache.put(templateFilePath, fileTemplateLoader);
        } catch (IOException e) {
            throw new FreeMarkerException("fileTemplateLoader init error!", e);
        }
        config.setTemplateLoader(fileTemplateLoader);
        configurationCache.put(templateFilePath, config);
        return config;
    }

    /**
     * 获取模板
     */
    public static String getContent(String fileName, Object data) {
        String templatePath = getPDFTemplatePath(fileName);
        String templateFileName = getTemplateName(templatePath);
        String templateFilePath = getTemplatePath(templatePath);
        if (StringUtils.isEmpty(templatePath)) {
            throw new FreeMarkerException("templatePath can not be empty!");
        }
        try {
            Template template = getConfiguration(templateFilePath).getTemplate(templateFileName);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            writer.flush();
            return writer.toString();
        } catch (Exception ex) {
            throw new FreeMarkerException("FreeMarkerUtil process fail", ex);
        }
    }

    private static String getTemplatePath(String templatePath) {
        if (StringUtils.isEmpty(templatePath)) {
            return "";
        }
        if (templatePath.contains(WINDOWS_SPLIT)) {
            return templatePath.substring(0, templatePath.lastIndexOf(WINDOWS_SPLIT));
        }
        return templatePath.substring(0, templatePath.lastIndexOf("/"));
    }

    private static String getTemplateName(String templatePath) {
        if (StringUtils.isEmpty(templatePath)) {
            return "";
        }
        if (templatePath.contains(WINDOWS_SPLIT)) {
            return templatePath.substring(templatePath.lastIndexOf(WINDOWS_SPLIT) + 1);
        }
        return templatePath.substring(templatePath.lastIndexOf("/") + 1);
    }

    /**
     * 获取模板路径, 按照文件名获取, 带后缀不带后缀都行
     *
     * @param fileName 模板文件名
     * @return 匹配到的模板名
     */
    public static String getPDFTemplatePath(String fileName) {
        String path = PathUtil.getClassResPath("templates");
        // 确认文件
        if (!fileName.contains(".")) {
            fileName = fileName + ".ftl";
        }
        File file = new File(path + fileName);
        if (!file.isFile()) {
            throw new PDFException("Template file " + fileName + " doesn't exist, pls check!");
        }
        return file.getAbsolutePath();
    }
}

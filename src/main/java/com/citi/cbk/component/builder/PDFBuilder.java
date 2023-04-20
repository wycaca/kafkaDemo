package com.citi.cbk.component.builder;

import com.citi.cbk.exception.PDFException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.ClassUtils;

import java.io.IOException;

/**
 * 设置页面附加属性
 */
@Slf4j
public class PDFBuilder extends PdfPageEventHelper {
    //字体文件名
    private String fontFileName;
    // 基础字体对象
    private BaseFont bf;
    // 利用基础字体生成的字体对象，一般用于生成中文文字
    private Font fontDetail;
    //文档字体大小
    private int fontSize = 12;
    //模板
    private PdfTemplate template;
    //数据实体
    private Object data;
    //页眉页脚定制接口
    private HeaderFooterBuilder headerFooterBuilder;

    //不允许空的构造方法
    private PDFBuilder() {
    }

    public PDFBuilder(HeaderFooterBuilder headerFooterBuilder) {
        this(headerFooterBuilder, null);
    }

    public PDFBuilder(HeaderFooterBuilder headerFooterBuilder, Object data) {
        this(headerFooterBuilder, data, "ping_fang_regular.ttf");
    }

    public PDFBuilder(HeaderFooterBuilder headerFooterBuilder, Object data, String fontFileName) {
        this(headerFooterBuilder, data, fontFileName, 12);
    }

    public PDFBuilder(HeaderFooterBuilder headerFooterBuilder, Object data, String fontFileName, int fontSize) {
        this.headerFooterBuilder = headerFooterBuilder;
        this.data = data;
        this.fontFileName = fontFileName;
        this.fontSize = fontSize;
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        template = writer.getDirectContent().createTemplate(50, 50);
    }

    /**
     * 关闭每页的时候，写入页眉,页脚等
     */
    public void onEndPage(PdfWriter writer, Document document) {
        this.addPage(writer, document);
    }

    //加分页
    private void addPage(PdfWriter writer, Document document) {
        if (headerFooterBuilder != null) {
            //1.初始化字体
            initFont();
//            //2.写入页眉
//            headerFooterBuilder.writeHeader(writer, document, data, fontDetail, template);
//            //3.写入前半部分页脚
//            headerFooterBuilder.writeFooter(writer, document, data, fontDetail, template);
        }
    }

    /**
     * 关闭文档时，替换模板，完成整个页眉页脚组件
     */
    public void onCloseDocument(PdfWriter writer, Document document) {
        if (headerFooterBuilder != null) {
            template.beginText();
            template.setFontAndSize(bf, fontSize);
            String replace = headerFooterBuilder.getReplaceOfTemplate(writer, document, data);
            template.showText(replace);
            template.endText();
            template.closePath();
        }
    }

    /**
     * 初始化字体
     */
    private void initFont() {
        if (StringUtils.isEmpty(fontFileName)) {
            throw new PDFException("Pls set PDF font!");
        }
        try {
            if (bf == null) {
                //添加字体，以支持中文
                String classpath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
                String fontPath = classpath + "fonts/" + fontFileName;
                //创建基础字体
                bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
            if (fontDetail == null) {
                fontDetail = new Font(bf, fontSize, Font.NORMAL);// 数据体字体
                log.info("PDF font init finished!");
            }
        } catch (DocumentException | IOException e) {
            log.error("font init error{}", ExceptionUtils.getStackTrace(e));
            throw new PDFException("font set error", e);
        }
    }

    public int getPresentFontSize() {
        return fontSize;
    }

    public void setPresentFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFileName() {
        return fontFileName;
    }

    public void setFontFileName(String fontFileName) {
        this.fontFileName = fontFileName;
    }
}
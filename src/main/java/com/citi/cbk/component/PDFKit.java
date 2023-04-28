package com.citi.cbk.component;

import com.citi.cbk.component.builder.HeaderFooterBuilder;
import com.citi.cbk.component.builder.PDFBuilder;
import com.citi.cbk.exception.PDFException;
import com.citi.cbk.util.FontUtil;
import com.citi.cbk.util.FreeMarkerUtil;
import com.citi.cbk.util.IOUtil;
import com.citi.cbk.util.PathUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Setter
@Getter
public class PDFKit {

    //PDF页眉、页脚定制工具
    private HeaderFooterBuilder headerFooterBuilder;
    private String saveFilePath;

    /**
     * @param templateFileName 模板文件名
     * @param PDFFileName      输出PDF文件名
     * @param data             模板所需要的数据
     * @description 导出pdf到文件
     */
    public String exportToFile(String templateFileName, String PDFFileName, Object data) {
        String htmlData =  FreeMarkerUtil.getContent(templateFileName, data);
        if (StringUtils.isEmpty(saveFilePath)) {
            saveFilePath = getDefaultSavePath(PDFFileName);
        }
        File file = new File(saveFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // 检查文件是否存在, 存在则删除重新生成
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.error("create file: {} error. {}", file.getPath(), ExceptionUtils.getStackTrace(e));
        }
        FileOutputStream outputStream = null;
        try {
            // 设置输出路径
            outputStream = new FileOutputStream(saveFilePath);
            // 设置文档大小
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // 设置页眉页脚
            PDFBuilder builder = new PDFBuilder(headerFooterBuilder, data);
            builder.setPresentFontSize(10);
            writer.setPageEvent(builder);

            //输出为PDF文件
            convertToPDF(writer, document, htmlData);
//            convertToPDFWithCSS(writer, document, htmlData);
//            convertToPDFCss(outputStream, htmlData);
        } catch (Exception ex) {
            throw new PDFException("PDF export to File fail", ex);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        return saveFilePath;
    }

    /**
     * 生成PDF到输出流中（ServletOutputStream用于下载PDF）
     *
     * @param ftlPath  ftl模板文件的路径（不含文件名）
     * @param data     输入到FTL中的数据
     * @param response HttpServletResponse
     * @return
     */
//    public OutputStream exportToResponse(String ftlPath, Object data, HttpServletResponse response) {
//        String html = FreeMarkerUtil.getContent(ftlPath, data);
//        try {
//            OutputStream out = null;
//            ITextRenderer render = null;
//            out = response.getOutputStream();
//            //设置文档大小
//            Document document = new Document(PageSize.A4);
//            PdfWriter writer = PdfWriter.getInstance(document, out);
//            //设置页眉页脚
//            PDFBuilder builder = new PDFBuilder(headerFooterBuilder, data);
//            writer.setPageEvent(builder);
//            //输出为PDF文件
//            convertToPDF(writer, document, html);
//            return out;
//        } catch (Exception ex) {
//            throw new PDFException("PDF export to response fail", ex);
//        }
//    }

    /**
     * PDF文件生成
     * 不支持CSS
     */
    private void convertToPDF(PdfWriter writer, Document document, String htmlString) {
        //获取字体路径
        String fontPath = getFontPath();
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(htmlString.getBytes()),
                    XMLWorkerHelper.class.getResourceAsStream("/default.css"),
                    StandardCharsets.UTF_8, new XMLWorkerFontProvider(fontPath));
        } catch (IOException e) {
            log.error("error: ", e);
            throw new PDFException("PDF文件生成异常", e);
        } finally {
            document.close();
        }
    }

    /**
     * PDF文件生成
     * 支持CSS
     */
    private void convertToPDFWithCSS(PdfWriter writer, Document document, String htmlString) {
        // 获取字体路径
        String fontPath = getFontPath();
        // todo 测试读取css
        // CSS 路径
        String cssSource = "F:\\workSpace\\citi\\kafka-demo\\src\\main\\resources\\templates\\demo.css";
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(htmlString.getBytes()),
                    new ByteArrayInputStream(IOUtil.readString(cssSource).getBytes()));
        } catch (IOException e) {
            log.error("error: ", e);
            throw new PDFException("PDF文件生成异常", e);
        } finally {
            document.close();
        }
    }

    /**
     * PDF文件生成
     * 支持CSS
     * 中文字体有问题
     */
    private void convertToPDFCss(FileOutputStream os, String htmlString) {
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        try {
            fontResolver.addFont(FontUtil.getFontPath("ping_fang_regular.ttf"), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.setDocumentFromString(htmlString);
            renderer.layout();
            renderer.createPDF(os);
        } catch (IOException e) {
            log.error("IO error ", e);
        }
        renderer.finishPDF();
    }

    /**
     * 创建默认保存路径
     */
    private String getDefaultSavePath(String fileName) {
        String pdfPath = getPdfPath();
        String saveFilePath = pdfPath + fileName;
        File f = new File(saveFilePath);
        if (!f.getParentFile().exists()) {
            f.mkdirs();
        }
        return saveFilePath;
    }

    /**
     * 获取字体设置路径
     */
    public static String getFontPath() {
        return PathUtil.getClassResPath("fonts");
    }

    /**
     * 获取pdf路径
     */
    public static String getPdfPath() {
        return PathUtil.getClassParentPath("pdf");
    }
}
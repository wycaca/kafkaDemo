package com.citi.cbk.util;

import com.citi.cbk.component.PDFHeaderFooter;
import com.citi.cbk.component.PDFKit;
import com.citi.cbk.entity.Currency;
import com.citi.cbk.entity.TableMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PdfCreator {

    public static String createPDF(String templateFileName, Object data, String PDFFileName) {
        //pdf保存路径
        try {
            //设置自定义PDF页眉页脚工具类
            PDFHeaderFooter headerFooter = new PDFHeaderFooter();
            PDFKit kit = new PDFKit();
            kit.setHeaderFooterBuilder(headerFooter);
            //设置输出路径
            String classpath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            kit.setSaveFilePath(classpath + "/pdf/" + PDFFileName);
            return kit.exportToFile(templateFileName, PDFFileName, data);
        } catch (Exception e) {
            log.error("create PDF failed: {}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put("tel", "111111222222");
//        PdfCreator.createPDF("phone-advice", params, "phone-advice.pdf");

        TableMsg tableMsg = new TableMsg();
        List<Currency> currencies = new ArrayList<>();
        tableMsg.setName("asd");
        tableMsg.setTel("1111112222222");
        tableMsg.setEmail("1111111111@email.com");
        tableMsg.setAddress("aaaaaaaaaaaaaaaa");
        tableMsg.setCurrencies(currencies);

        Currency currency = new Currency();
        currency.setName("hkd");
        currency.setValue("$125,596,800.00");
        currency.setValueUsd("$16,000,000.00");
        currency.setAllocation("$10,651,537.00");
        currencies.add(currency);

        currency = new Currency();
        currency.setName("usd");
        currency.setValue("$225,596,800.00");
        currency.setValueUsd("$26,000,000.00");
        currency.setAllocation("$20,651,537.00");
        currencies.add(currency);
        currency = new Currency();
        currency.setName("sgd");
        currency.setValue("$325,596,800.00");
        currency.setValueUsd("$36,000,000.00");
        currency.setAllocation("$30,651,537.00");
        currencies.add(currency);
        currency = new Currency();
        currency.setName("cny");
        currency.setValue("$325,596,800.00");
        currency.setValueUsd("$36,000,000.00");
        currency.setAllocation("$30,651,537.00");
        currencies.add(currency);

//        PdfCreator.createPDF("demo", tableMsg, "demo.pdf");
//        System.out.println(javaToJsonString(tableMsg));
    }

    private static String javaToJsonString(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

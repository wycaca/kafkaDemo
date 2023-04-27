package com.citi.cbk.controller;

import com.citi.cbk.entity.EditMsg;
import com.citi.cbk.entity.TableMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "test/")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @PostMapping(path = "send")
    public String send(@RequestBody EditMsg msg) throws JsonProcessingException {
        template.send("topic1", msg).addCallback(success -> {
            // 消息发送到的topic
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            logger.info("Provider send msg: {}, {}", msg, topic + "-" + partition + "-" + offset);
        }, failure -> {
            logger.error("Provider send msg failed: {}", failure.getMessage());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("code", "200");
        response.put("data", msg.getId());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }

    @PostMapping(path = "/table/send")
    public String tableSend(@RequestBody TableMsg msg) {
        template.send("test-topic", msg).addCallback(success -> {
            // 消息发送到的topic
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            logger.info("Provider send msg: {}, {}", msg, topic + "-" + partition + "-" + offset);
        }, failure -> {
            logger.error("Provider send msg failed: {}", failure.getMessage());
        });
        return "send success";
    }

    @GetMapping("pdf")
    public ResponseEntity<Resource> download(@RequestParam String type, @RequestParam String id) {
        String classpath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String pdfType = "phone-advice-";
        if ("phone".equals(type)) {
            pdfType = "phone-advice-";
        }
        String path = classpath + "pdf/" + pdfType +  id + ".pdf";

        File file = new File(path);
        // 检查文件是否存在, 不存在则提示
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(path)
                .build().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new FileSystemResource(path));
    }
}

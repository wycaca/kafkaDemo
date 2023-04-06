package com.citi.cbk.controller;

import com.citi.cbk.entity.EditMsg;
import com.citi.cbk.entity.TableMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "test/")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @PostMapping(path = "send")
    public String send(@RequestBody EditMsg msg) {
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
        return "send success";
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

}

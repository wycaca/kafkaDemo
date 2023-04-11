package com.citi.cbk.consumer;

import com.citi.cbk.entity.EditMsg;
import com.citi.cbk.entity.TableMsg;
import com.citi.cbk.util.PdfCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MsgListener {
    private final Logger logger = LoggerFactory.getLogger(MsgListener.class);

    @KafkaListener(id = "sampleListener", topics = "topic1")
    void listenForEditMsg(EditMsg msg) {
        logger.info("Creating the tel advice, {}", msg.getTel());
        createTelAdvice(msg);
    }

    @KafkaListener(id = "tableMsgListener", topics = "test-topic")
    void listenForTableMsg(TableMsg msg) {
        PdfCreator.createPDF("demo", msg, "demo-" + msg.getName() + ".pdf");
    }

    private void createTelAdvice(EditMsg msg) {
        Map<String, Object> params = new HashMap<>();
        params.put("tel", msg.getTel());
        // todo handle type
        PdfCreator.createPDF("phone-advice", params, "phone-advice-" + msg.getId() + ".pdf");
    }
}

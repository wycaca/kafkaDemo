package com.citi.cbk.consumer;

import com.citi.cbk.entity.EditMsg;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MsgListener {
    private final Logger logger = LoggerFactory.getLogger(MsgListener.class);

    @KafkaListener(id ="sampleListener", topics = "topic1")
    void listenForEditMsg(EditMsg msg) {
        if (StringUtils.isNotEmpty(msg.getTel())) {
            logger.info("\nDear Valued Client, \n" +
                    "Update of Telephone Number \n" +
                    "Your new telephone number is: {} \n" +
                    "Citibank", msg.getTel());
        }
        if (StringUtils.isNotEmpty(msg.getEmail())) {
            logger.info("\nDear Valued Client, \n" +
                    "Update of Email Address \n" +
                    "Your new email address is: {} \n" +
                    "Citibank", msg.getEmail());
        }
        if (StringUtils.isNotEmpty(msg.getAddress())) {
            logger.info("\nDear Valued Client, \n" +
                    "Update of Address \n" +
                    "Your new address is: {} \n" +
                    "Citibank", msg.getAddress());
        }
    }
}

package com.citi.cbk.consumer;

import com.citi.cbk.entity.EditMsg;
import com.citi.cbk.entity.TableMsg;
import com.citi.cbk.util.PdfCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class MsgListener {
    private final Logger logger = LoggerFactory.getLogger(MsgListener.class);

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String sendMail;

    @KafkaListener(id = "telListener", topics = "topic1")
    void listenForEditMsg(EditMsg msg) {
        logger.info("Creating the tel advice, {}", msg.getTel());
        String path = createTelAdvice(msg);
        if (path != null) {
            try {
                sendMail(path);
            } catch (MessagingException e) {
                logger.error("send email error, ", e);
            }
        }

    }

    @KafkaListener(id = "tableMsgListener", topics = "test-topic")
    void listenForTableMsg(TableMsg msg) {
        PdfCreator.createPDF("demo", msg, "demo-" + msg.getName() + ".pdf");
    }

    private String createTelAdvice(EditMsg msg) {
        Map<String, Object> params = new HashMap<>();
        params.put("tel", msg.getTel());
        // todo handle type
        return PdfCreator.createPDF("phone-advice", params, "phone-advice-" + msg.getId() + ".pdf");
    }

    public void sendMail(String filePath) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(sendMail);
        messageHelper.setTo("rzyfbj119@163.com");
        messageHelper.setSubject("Tel Advice");
        messageHelper.setText("Please check the advice!");
        File file = new File(filePath);
        messageHelper.addAttachment(file.getName(), file);
        mailSender.send(mimeMessage);
    }
}

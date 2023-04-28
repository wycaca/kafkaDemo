package com.citi.cbk.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
@Slf4j
public class MailUtil {

    private static MailUtil mailUtil;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @PostConstruct
    public void init() {
        mailUtil = this;
        mailUtil.mailSender = this.mailSender;
    }

    public static void sendMail(String toMail, String filePath) {
        MimeMessage mimeMessage = mailUtil.mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("rzyfbj119@163.com");
            messageHelper.setTo(toMail);
            messageHelper.setSubject("Tel Advice");
            messageHelper.setText("Please check the advice!");
            File file = new File(filePath);
            messageHelper.addAttachment(file.getName(), file);
        } catch (MessagingException e) {
            log.error("send email error, ", e);
        }
        mailUtil.mailSender.send(mimeMessage);
    }
}

package me.wjz.creeperhub.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.exception.CreeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String text) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);//是否有html内容
        helper.setFrom("wjz_p@qq.com");
        emailSender.send(message);
    }
}

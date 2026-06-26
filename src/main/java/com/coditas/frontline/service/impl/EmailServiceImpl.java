package com.coditas.frontline.service.impl;

import com.coditas.frontline.dto.request.EmailDetails;
import com.coditas.frontline.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;
    public String sendSimpleMail(EmailDetails details) {
        log.info("Email service started");
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
            log.info("Email sent successfully");
            return "Mail Sent Successfully";
        } catch (Exception e) {
            log.error("Error occurred while sending email not sent");
            return "Error while sending mail";
        }
    }

    public String sendMailWithAttachment(EmailDetails details) {
        log.info("Email service started");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setText(details.getMsgBody());
            helper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            helper.addAttachment(file.getFilename(), file);
            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully");
            return "Mail Sent Successfully";
        } catch (MessagingException e) {
            log.error("Error occurred while sending email not sent");
            return "Error while sending mail";
        }
    }
}

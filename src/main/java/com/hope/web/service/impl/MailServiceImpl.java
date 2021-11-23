package com.hope.web.mail.impl;

import com.hope.web.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class MailServiceImpl implements MailService {

    @Value("#{systemEnvironment['FROM_EMAIL']}")
    private String fromAddress;

    @Value("${web-site-link}")
    private String webSiteLink;

    private JavaMailSender mailSender;

    private SpringTemplateEngine thymeleaf;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine thymeleaf) {
        this.mailSender = mailSender;
        this.thymeleaf = thymeleaf;
    }

    public void sendEmail(String toAddress, String subject, String message) throws MessagingException {

        Context ctx = new Context();
        ctx.setVariable("subject", subject);
        ctx.setVariable("message", message);
        ctx.setVariable("villaimg", "villaimg");
        ctx.setVariable("webSiteLink", webSiteLink);

        String emailContent = thymeleaf.process("mail.html", ctx);

        MimeMessage mimeMessage =  mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(fromAddress);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setCc(fromAddress);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(emailContent, true);

        mimeMessageHelper.addInline("villaimg", new ClassPathResource("static/img/logo_villa3.svg"), "image/svg+xml");

        mailSender.send(mimeMessage);
    }
}


package com.hope.web.mail.impl;

import com.hope.web.mail.MailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
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
import java.io.IOException;

@Slf4j
@Component
public class MailServiceImpl implements MailService {

    @Value("#{systemEnvironment['FROM_EMAIL']}")
    private String fromAddress;

    @Value("#{systemEnvironment['SENDGRID_API_KEY']}")
    private String apiKey;

    private JavaMailSender mailSender;

    private SpringTemplateEngine thymeleaf;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine thymeleaf) {
        this.mailSender = mailSender;
        this.thymeleaf = thymeleaf;
    }

    public void sendEmail(String toAddress, String subject, String message) throws IOException{
        Email fromEmailAddress = new Email(fromAddress);
        Email toEmailAddress = new Email(toAddress);

        //"This could be built as HTML message for a better user experience"
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(fromEmailAddress, subject, toEmailAddress, content);
        mail.setReplyTo(new Email(toAddress));
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            final Response response = sg.api(request);
            log.info(String.format("Email sent successfully, status code: %d, body: %s", response.getStatusCode(), response.getBody()));
            if(response.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new IOException(response.getBody());
            }
        }catch(IOException ex) {
            log.error(String.format("Error while trying to send email, message: %s", ex.getMessage()));
            throw ex;
        }
    }

    public void sendOwnerEmail(String toAddress, String subject, String message) throws MessagingException {

        Context ctx = new Context();
        ctx.setVariable("subject", subject);
        ctx.setVariable("message", message);
        ctx.setVariable("villaimg", "villaimg");

        String emailContent = thymeleaf.process("mail.html", ctx);

        MimeMessage mimeMessage =  mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(fromAddress);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setCc(fromAddress);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(emailContent, true);

        mimeMessageHelper.addInline("villaimg", new ClassPathResource("static/img/img.png"), "image/png");

        mailSender.send(mimeMessage);
    }
}


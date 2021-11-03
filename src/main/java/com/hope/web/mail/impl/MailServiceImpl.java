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
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MailServiceImpl implements MailService {

    @Value("#{systemEnvironment['FROM_EMAIL']}")
    String fromAddress;

    @Value("#{systemEnvironment['SENDGRID_API_KEY']}")
    String apiKey;

    MailSender mailSender;

    @Autowired
    public MailServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
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

    public void sendOwnerEmail(String toAddress, String subject, String message) throws MailException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(subject);
        mailMessage.setTo(toAddress, fromAddress);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}


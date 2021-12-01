package com.hope.web.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    MailServiceImpl mailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Spy
    @InjectMocks
    private SpringTemplateEngine templateEngine;

    @BeforeEach
    public void setup() {
        mailService = new MailServiceImpl(mailSender, templateEngine);//, ctx);
        ReflectionTestUtils.setField(mailService, "fromAddress", "from@mail.com");
        ReflectionTestUtils.setField(mailService, "webSiteLink", "http://www.mock.com");
    }

    @Test
    public void test_sendEmail_ShouldThrowMessagingException() {
        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Exception exception = assertThrows(MessagingException.class, () -> {
            mailService.sendEmail("@badmail.com", "mock subject", "a Mock message");
        });
    }


    @Test
    public void test_sendEmail_shouldVerifySendMethodIsInvoked() {
        try {
            Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            mailService.sendEmail("to@mail.com", "mock subject", "a Mock message");
            Mockito.verify(mailSender, times(1)).send(Mockito.any(MimeMessage.class));
        } catch(MessagingException ex) {

        }
    }
}
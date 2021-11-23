package com.hope.web.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface MailService {
    void sendEmail(String from, String subject, String message) throws MessagingException;
}

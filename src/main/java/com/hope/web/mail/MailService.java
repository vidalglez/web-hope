package com.hope.web.mail;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface MailService {
    void sendEmail(String from, String subject, String message) throws IOException;
    void sendOwnerEmail(String from, String subject, String message);
}

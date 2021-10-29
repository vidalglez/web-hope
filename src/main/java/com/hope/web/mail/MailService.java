package com.hope.web.mail;

import java.io.IOException;

public interface MailService {
    public void sendEmail(String from, String subject, String message) throws IOException;
}

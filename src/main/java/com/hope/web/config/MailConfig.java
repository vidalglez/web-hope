package com.hope.web.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class MailConfig {

    @Value("#{systemEnvironment['MAIL_HOST']}")
    private String host;

    @Value("#{systemEnvironment['MAIL_PORT']}")
    private String port;

    @Value("#{systemEnvironment['MAIL_USERNAME']}")
    private String username;

    @Value("#{systemEnvironment['MAIL_PASSWORD']}")
    private String password;
}

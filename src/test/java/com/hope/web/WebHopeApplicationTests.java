package com.hope.web;

import com.hope.web.config.MailConfig;
import com.hope.web.controller.InitialController;
import com.hope.web.controller.MailController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebHopeApplicationTests {

	@Autowired
	private InitialController initialController;

	@Autowired
	private MailConfig mailConfig;

	@MockBean
	private JavaMailSender mailSender;

	@MockBean
	SpringTemplateEngine thymeleaf;

	@Autowired
	private MailController mailController;

	@TestConfiguration
	public static class EarlyConfig {

		@MockBean
		private MailConfig mailConfig;

		@PostConstruct
		public void initMock() {
			Mockito.when(mailConfig.getPort()).thenReturn("4567");
			Mockito.when(mailConfig.getPort()).thenReturn("4567");
		}
	}

	@Test
	void contextLoads() {
		assertThat(initialController).isNotNull();
		assertThat(mailController).isNotNull();
	}

}

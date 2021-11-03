package com.hope.web;

import com.hope.web.config.MailConfig;
import com.hope.web.controller.InitialController;
import com.hope.web.controller.MailController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebHopeApplicationTests {

	@Autowired
	private InitialController initialController;

	@MockBean
	private MailConfig mailConfig;

	@MockBean
	private MailSender mailSender;

	@Autowired
	private MailController mailController;

	@BeforeEach
	public void setup() {
		Mockito.when(mailConfig.getPort()).thenReturn("0");
	}

	@Test
	void contextLoads() {
		assertThat(initialController).isNotNull();
		assertThat(mailController).isNotNull();
	}

}

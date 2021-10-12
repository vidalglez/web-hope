package com.hope.web;

import com.hope.web.controller.InitialController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebHopeApplicationTests {

	@Autowired
	private InitialController initialController;

	@Test
	void contextLoads() {
		assertThat(initialController).isNotNull();
	}

}

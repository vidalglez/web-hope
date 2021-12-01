package com.hope.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hope.web.config.MailConfig;
import com.hope.web.dto.EmailDataDTO;
import com.hope.web.service.MailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSendException;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MailControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MailService mailService;

    @Test
    public void test_mailController_shouldReturnSuccessfulResponse() {
        EmailDataDTO mockDTO = new EmailDataDTO();
        mockDTO.setToEmail("test@mock.com");
        mockDTO.setMessage("Mock message");
        mockDTO.setSubject("Mock Name");

        try {
            doNothing().when(mailService).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

            mvc.perform(post("/mail/send")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(mockDTO)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.result", is("Mail has been sent successfully!!!")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_mailController_shouldValidatePayload() {
        EmailDataDTO mockDTO = new EmailDataDTO();
        mockDTO.setMessage("Mock message");
        mockDTO.setSubject("Mock Name");
        try {
            mvc.perform(post("/mail/send")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(mockDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[0]", is("toEmail must not be null")))
                    .andExpect(jsonPath("$.timestamp").exists());
        }catch(Exception ex) {
        }
    }

    @Test
    public void test_mailController_shouldValidateEmailAddress() {
        EmailDataDTO mockDTO = new EmailDataDTO();
        mockDTO.setToEmail("@mockmail.com");
        mockDTO.setMessage("Mock message");
        mockDTO.setSubject("Mock Name");
        try {
            mvc.perform(post("/mail/send")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(mockDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[0]", is("toEmail must be a well-formed email address")))
                    .andExpect(jsonPath("$.timestamp").exists());
        }catch(Exception ex) {
        }
    }

    @Test
    public void test_mailController_shouldHandleMailSendException() {
        EmailDataDTO mockDTO = new EmailDataDTO();
        mockDTO.setToEmail("test@mock.com");
        mockDTO.setMessage("Mock message");
        mockDTO.setSubject("Mock Name");
        try{
            doThrow(new MailSendException("Mock error")).when(mailService)
                    .sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

            mvc.perform(post("/mail/send")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(asJsonString(mockDTO)))
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$.errors[0]", is("El email no pudo ser enviado, por favor intentelo mas tarde")));
        }catch(Exception ex) {

        }
    }

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


    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
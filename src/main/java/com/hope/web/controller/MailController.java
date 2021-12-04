package com.hope.web.controller;

import com.hope.web.dto.EmailDataDTO;
import com.hope.web.dto.MailResultDTO;
import com.hope.web.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/mail")
public class MailController {

    MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping(value="/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public  ResponseEntity<MailResultDTO> sendEmailFromClient(@Valid @RequestBody EmailDataDTO emailData) throws MessagingException {
        log.info(String.format("Received email data: %s", emailData.toString()));

        mailService.sendEmail(emailData.getToEmail(), emailData.getSubject(), emailData.getMessage());
        log.info("Mail has been sent successfully!!!");
        MailResultDTO result = new MailResultDTO();
        result.setStatus(HttpStatus.OK.value());
        result.setErrors(new ArrayList<>());
        result.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.accepted().body(result);
    }
}

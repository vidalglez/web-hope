package com.hope.web.controller;

import com.hope.web.dto.EmailDataDTO;
import com.hope.web.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/mail")
public class MailController {

    MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmail(@RequestBody EmailDataDTO emailData) {
        log.info(String.format("Received email data: %s", emailData.toString()));
        try {
            mailService.sendEmail(emailData.getToEmail(), emailData.getSubject(), emailData.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }




}

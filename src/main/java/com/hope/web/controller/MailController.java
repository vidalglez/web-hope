package com.hope.web.controller;

import com.hope.web.dto.EmailDataDTO;
import com.hope.web.dto.ResultDTO;
import com.hope.web.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

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
    public  ResponseEntity<ResultDTO> sendEmailFromClient(@Valid @RequestBody EmailDataDTO emailData) throws MessagingException {
        log.info(String.format("Received email data: %s", emailData.toString()));

        mailService.sendEmail(emailData.getToEmail(), emailData.getSubject(), emailData.getMessage());
        log.info("Mail has been sent successfully!!!");

        return ResponseEntity.accepted().body(new ResultDTO("Mail has been sent successfully!!!"));
    }
}

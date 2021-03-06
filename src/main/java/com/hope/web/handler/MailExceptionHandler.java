package com.hope.web.handler;

import com.hope.web.dto.MailResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class MailExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<MailResultDTO> handleMailDataException(MailSendException ex) {
        log.error(String.format("%s - %s", "handleMailDataException", ex.getMessage()));

        MailResultDTO errorResponse = new MailResultDTO();
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setErrors(Arrays.asList("El email no pudo ser enviado, por favor intentelo mas tarde"));

        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<MailResultDTO> handleMethodArgumentInvalid(MethodArgumentNotValidException ex) {
        log.error(String.format("%s - %s", "handleMethodArgumentInvalid", ex.getMessage()));

        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(message -> String.format("%s %s", message.getField(), message.getDefaultMessage())).collect(Collectors.toList());
        MailResultDTO errorResponse = new MailResultDTO();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrors(errors);
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<MailResultDTO>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}

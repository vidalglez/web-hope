package com.hope.web.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class EmailDataDTO {

    @NotNull
    @Email
    private String toEmail;

    @NotNull
    private String subject;

    @NotNull
    private String message;
}

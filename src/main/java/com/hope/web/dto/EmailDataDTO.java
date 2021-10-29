package com.hope.web.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EmailDataDTO {
    private String toEmail;
    private String subject;
    private String message;
}

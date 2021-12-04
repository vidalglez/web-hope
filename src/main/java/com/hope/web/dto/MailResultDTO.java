package com.hope.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailResultDTO {

    private int status;
    private List<String> errors;
    private long timestamp;

}

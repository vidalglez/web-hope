package com.hope.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
public class InitialController {

    @GetMapping("/message")
    @ResponseBody
    public String getInitMessage() {
        return "Init controller deployed successfully";
    }

}

package com.oefa.backend.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
public class HomeController {
    @GetMapping("/home")
    public  String init() {
        return "init server..";
    }
}

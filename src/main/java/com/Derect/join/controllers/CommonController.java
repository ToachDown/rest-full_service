package com.Derect.join.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

    @GetMapping
    public String homPage(){
        return "homePage";
    }
}

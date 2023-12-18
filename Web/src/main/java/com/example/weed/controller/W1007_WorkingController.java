package com.example.weed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class W1007_WorkingController {

    @GetMapping("/working")
    public String calendar() {
        return "W1007_working";
    }

}

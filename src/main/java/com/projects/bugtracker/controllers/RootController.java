package com.projects.bugtracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping({"", "/"})
    public String root(){
        return "index.html";
    }
}

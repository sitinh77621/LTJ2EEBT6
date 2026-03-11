package com.example.BaiTap2.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "Xin chào Spring Boot 👋");
        return "index"; // trỏ tới index.html
    }
    
    @GetMapping("/home")
    @ResponseBody
    public String home(Principal principal) {
        return "Hello, " + principal.getName();
    }
}



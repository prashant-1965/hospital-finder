package com.healthcare.finder.doctorHospitalFinder.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginUi {
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }
}

package com.healthcare.finder.doctorHospitalFinder.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpFormUi {
    @GetMapping("/signUpForm")
    public String signUpForm(){
        return "signUp";
    }
}

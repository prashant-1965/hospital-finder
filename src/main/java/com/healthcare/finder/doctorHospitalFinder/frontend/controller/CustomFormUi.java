package com.healthcare.finder.doctorHospitalFinder.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomFormUi {
    @GetMapping("/customise")
    public String customise(){
        return "customiseForm";
    }
}

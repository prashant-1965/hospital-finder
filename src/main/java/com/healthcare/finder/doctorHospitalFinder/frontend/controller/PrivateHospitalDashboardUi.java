package com.healthcare.finder.doctorHospitalFinder.frontend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
public class PrivateHospitalDashboardUi {

    @GetMapping("/redirectPrivateHospital")
    public String redirectPrivateHospital(@RequestParam Map<String,String> customData, Model model) {
        // Pass customData into Thymeleaf page
        model.addAttribute("customData", customData);
        return "privateHospitalDashBoard";
    }
}


package com.healthcare.finder.doctorHospitalFinder.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AdminDashboardUi {
    @GetMapping("/adminHome")
    public String getAdminDashBoard(){
        return "adminDashboard";
    }
}

package com.healthcare.finder.doctorHospitalFinder.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoctorDashboardUi {
    @GetMapping("/doctorHome")
    public String getAdminDashBoard(){
        return "doctorDashboard";
    }
}

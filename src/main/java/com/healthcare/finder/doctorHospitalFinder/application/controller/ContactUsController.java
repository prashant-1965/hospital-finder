package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.DoctorRegisterDto;

import com.healthcare.finder.doctorHospitalFinder.application.services.DoctorApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contactUs")
public class ContactUsController {
    @Autowired
    private DoctorApplicationService doctorApplicationService;

    @PostMapping("/doctorRegistrationRequest")
    public ResponseEntity<String> doctorRegistrationRequest(@RequestBody DoctorRegisterDto doctorRegisterDto){
        return ResponseEntity.status(200).body(doctorApplicationService.addDoctorApplicationRequest(doctorRegisterDto));
    }
}
